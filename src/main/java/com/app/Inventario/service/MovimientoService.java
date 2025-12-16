package com.app.Inventario.service;

import com.app.Inventario.mapper.MovimientoMapper;
import com.app.Inventario.model.dto.request.DetalleMovimientoDTO;
import com.app.Inventario.model.dto.request.RegistroMovimientoDTO;
import com.app.Inventario.model.dto.response.MovimientoResponseDTO;
import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.entity.Movimiento;
import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.model.entity.SolicitudItems;
import com.app.Inventario.model.enums.EstadoBien;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.model.enums.TipoMovimiento;
import com.app.Inventario.repository.BienRepository;
import com.app.Inventario.repository.MovimientoRepository;
import com.app.Inventario.repository.SolicitudGilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final BienRepository bienRepository;
    private final MovimientoMapper movimientoMapper;
    private final SolicitudGilService solicitudGilService;
    private final SolicitudGilRepository solicitudGilRepository;

    @Transactional
    public List<MovimientoResponseDTO> registrarEntrada(RegistroMovimientoDTO request) {
        List<Movimiento> movimientosGuardados = new ArrayList<>();

        for (DetalleMovimientoDTO detalle : request.getDetalles()) {
            Bien bien = bienRepository.findById(detalle.getBienId())
                    .orElseThrow(() -> new RuntimeException("Bien no encontrado ID: " + detalle.getBienId()));


            BigDecimal saldoAnterior = bien.getStockActual();
            BigDecimal nuevoSaldo = saldoAnterior.add(detalle.getCantidad());

            bien.setStockActual(nuevoSaldo);
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) > 0) {
                bien.setEstado(EstadoBien.DISPONIBLE);
            }
            bienRepository.save(bien);


            Movimiento movimiento = Movimiento.builder()
                    .bien(bien)
                    .tipo(TipoMovimiento.ENTRADA)
                    .cantidad(detalle.getCantidad())
                    .valorUnitario(bien.getValorUnitario())
                    .valorTotal(detalle.getCantidad().multiply(bien.getValorUnitario()))
                    .saldoAnterior(saldoAnterior)
                    .saldoNuevo(nuevoSaldo)
                    .consecutivo(request.getConsecutivo())
                    .observaciones(detalle.getObservaciones())
                    .build();

            movimientosGuardados.add(movimientoRepository.save(movimiento));
        }
        return movimientoMapper.toResponseList(movimientosGuardados);
    }

    @Transactional
    public List<MovimientoResponseDTO> registrarSalida(RegistroMovimientoDTO request) {
        List<Movimiento> movimientosGuardados = new ArrayList<>();

        for (DetalleMovimientoDTO detalle : request.getDetalles()) {
            Bien bien = bienRepository.findById(detalle.getBienId())
                    .orElseThrow(() -> new RuntimeException("Bien no encontrado ID: " + detalle.getBienId()));

            if (bien.getStockActual().compareTo(detalle.getCantidad()) < 0) {
                throw new RuntimeException("Stock insuficiente para: " + bien.getNombre());
            }

            BigDecimal saldoAnterior = bien.getStockActual();
            BigDecimal nuevoSaldo = saldoAnterior.subtract(detalle.getCantidad());

            bien.setStockActual(nuevoSaldo);
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) == 0) {
                bien.setEstado(EstadoBien.SIN_STOCK);
            } else if (bien.getStockMinimo() != null && nuevoSaldo.compareTo(bien.getStockMinimo()) <= 0) {
                bien.setEstado(EstadoBien.BAJO_STOCK);
            }
            bienRepository.save(bien);

            Movimiento movimiento = Movimiento.builder()
                    .bien(bien)
                    .tipo(TipoMovimiento.SALIDA)
                    .cantidad(detalle.getCantidad())
                    .valorUnitario(bien.getValorUnitario())
                    .valorTotal(detalle.getCantidad().multiply(bien.getValorUnitario()))
                    .saldoAnterior(saldoAnterior)
                    .saldoNuevo(nuevoSaldo)
                    .fichaEntrega(request.getFichaEntrega())
                    .consecutivo(request.getConsecutivo())
                    .observaciones(detalle.getObservaciones())
                    .build();

            movimientosGuardados.add(movimientoRepository.save(movimiento));
        }
        return movimientoMapper.toResponseList(movimientosGuardados);
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> obtenerHistorialPorBien(Long bienId) {
        return movimientoMapper.toResponseList(movimientoRepository.findByBienIdOrderByFechaMovimientoDesc(bienId));
    }

    @Transactional
    public List<MovimientoResponseDTO> registrarEntradaMasivaGil(Long solicitudGilId) {

        SolicitudGil solicitudGil = solicitudGilService.obtenerEntidadPorId(solicitudGilId);

        if (solicitudGil.getEstado() != EstadoSolicitud.APROBADA) {
            throw new RuntimeException("El documento GIL-F-014 (Solicitud ID: " + solicitudGilId + ") no está APROBADO.");
        }

        if (solicitudGil.getFacturaGlobal() != null) {
            throw new RuntimeException("El documento GIL-F-014 (Solicitud ID: " + solicitudGilId + ") ya fue utilizado para Factura Global.");
        }

        List<Movimiento> movimientosGuardados = new ArrayList<>();

        for (SolicitudItems item : solicitudGil.getItems()) {

            Bien bien = item.getBien();

            if (bien == null) {
                throw new RuntimeException("Bien asociado al ítem de la solicitud no encontrado.");
            }

            BigDecimal cantidadEntrada = item.getCantidad();
            BigDecimal saldoAnterior = bien.getStockActual();
            BigDecimal nuevoSaldo = saldoAnterior.add(cantidadEntrada);

            BigDecimal valorUnitarioMovimiento = bien.getValorUnitario();

            bien.setStockActual(nuevoSaldo);
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) > 0) {
                bien.setEstado(EstadoBien.DISPONIBLE);
            }
            bienRepository.save(bien);

            Movimiento movimiento = Movimiento.builder()
                    .bien(bien)
                    .tipo(TipoMovimiento.ENTRADA)
                    .cantidad(cantidadEntrada)
                    .valorUnitario(valorUnitarioMovimiento)
                    .valorTotal(cantidadEntrada.multiply(valorUnitarioMovimiento))
                    .saldoAnterior(saldoAnterior)
                    .saldoNuevo(nuevoSaldo)
                    .consecutivo(solicitudGil.getCodigo())
                    .observaciones("Carga masiva desde Solicitud GIL-F-014 ID: " + solicitudGilId)
                    .build();

            movimientosGuardados.add(movimientoRepository.save(movimiento));
        }

        solicitudGil.setEstado(EstadoSolicitud.PROCESADA);
        solicitudGilRepository.save(solicitudGil);

        return movimientoMapper.toResponseList(movimientosGuardados);
    }
}