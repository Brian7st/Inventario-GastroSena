package com.app.Inventario.service;

import com.app.Inventario.mapper.PreFacturaDetalleMapper;
import com.app.Inventario.mapper.PreFacturaMapper;
import com.app.Inventario.model.dto.request.PreFacturaDetalleRequestDTO;
import com.app.Inventario.model.dto.request.PreFacturaRequestDTO;
import com.app.Inventario.model.dto.response.PreFacturaResponseDTO;
import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.entity.PreFactura;
import com.app.Inventario.model.entity.PreFacturaDetalle;
import com.app.Inventario.model.entity.ProgramaFormacion;
import com.app.Inventario.model.enums.EstadoPreFactura;
import com.app.Inventario.repository.BienRepository;
import com.app.Inventario.repository.PreFacturaDetalleRepository;
import com.app.Inventario.repository.PreFacturaRepository;
import com.app.Inventario.repository.ProgramaFormacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreFacturaService {

    private final PreFacturaRepository preFacturaRepository;
    private final PreFacturaDetalleRepository preFacturaDetalleRepository;
    private final ProgramaFormacionRepository programaFormacionRepository;
    private final BienRepository bienRepository;
    private final PreFacturaMapper preFacturaMapper;
    private final PreFacturaDetalleMapper preFacturaDetalleMapper;

    @Transactional
    public PreFacturaResponseDTO crearPreFactura(PreFacturaRequestDTO request) {
        if (preFacturaRepository.existsByNumero(request.getNumero())) {
            throw new RuntimeException("Ya existe una PreFactura con el número: " + request.getNumero());
        }

        ProgramaFormacion programa = programaFormacionRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa no encontrado ID: " + request.getProgramaId()));

        PreFactura nuevaPreFactura = preFacturaMapper.toEntity(request, programa);
        nuevaPreFactura.setFecha(LocalDateTime.now());
        nuevaPreFactura.setEstado(EstadoPreFactura.PENDIENTE);
        nuevaPreFactura.setDetalles(new ArrayList<>());

        BigDecimal totalGeneral = procesarDetalles(request.getDetalles(), nuevaPreFactura);
        nuevaPreFactura.setTotalPrefactura(totalGeneral);

        PreFactura guardada = preFacturaRepository.save(nuevaPreFactura);
        return mapToResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<PreFacturaResponseDTO> listarTodos() {
        return preFacturaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PreFacturaResponseDTO obtenerPorId(long id) {
        PreFactura pf = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreFactura no encontrada ID: " + id));
        return mapToResponse(pf);
    }

    @Transactional
    public PreFacturaResponseDTO actualizarPreFactura(long id, PreFacturaRequestDTO request) {
        PreFactura existente = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID no encontrado: " + id));

        if (existente.getEstado() != EstadoPreFactura.PENDIENTE) {
            throw new RuntimeException("Solo se pueden modificar en estado PENDIENTE.");
        }

        preFacturaDetalleRepository.deleteAll(existente.getDetalles());
        existente.getDetalles().clear();

        ProgramaFormacion programa = programaFormacionRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa no encontrado"));

        preFacturaMapper.updateEntityFromDTO(existente, request, programa);

        BigDecimal nuevoTotal = procesarDetalles(request.getDetalles(), existente);
        existente.setTotalPrefactura(nuevoTotal);

        return mapToResponse(preFacturaRepository.save(existente));
    }

    @Transactional
    public void anularPreFactura(long id) {
        PreFactura pf = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrada"));
        pf.setEstado(EstadoPreFactura.ANULADA);
        preFacturaRepository.save(pf);
    }

    @Transactional
    public PreFacturaResponseDTO validarPreFactura(long id) {
        PreFactura pf = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrada"));

        if (pf.getEstado() == EstadoPreFactura.PENDIENTE) {
            pf.setEstado(EstadoPreFactura.VALIDADA);
            pf = preFacturaRepository.save(pf);
        }
        return mapToResponse(pf);
    }

    private BigDecimal procesarDetalles(List<PreFacturaDetalleRequestDTO> detallesRequest, PreFactura preFactura) {
        BigDecimal total = BigDecimal.ZERO;
        for (PreFacturaDetalleRequestDTO dr : detallesRequest) {
            Bien bien = bienRepository.findById(dr.getBienId())
                    .orElseThrow(() -> new RuntimeException("Bien ID " + dr.getBienId() + " no existe"));

            PreFacturaDetalle detalle = preFacturaDetalleMapper.toEntity(dr, bien, preFactura);
            BigDecimal totalLinea = dr.getCantidad().multiply(dr.getPrecioAdjudicado());
            total = total.add(totalLinea);
            preFactura.getDetalles().add(detalle);
        }
        return total;
    }

    private PreFacturaResponseDTO mapToResponse(PreFactura pf) {
        if (pf.getProgramaFormacion() != null) pf.getProgramaFormacion().getId();
        if (pf.getDetalles() != null) pf.getDetalles().size();

        PreFacturaResponseDTO response = preFacturaMapper.toResponseDtoPreFactura(pf);

        if (pf.getDetalles() != null) {
            response.setDetalles(pf.getDetalles().stream()
                    .map(preFacturaDetalleMapper::toResponseDto)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    @Transactional(readOnly = true)
    public List<PreFacturaResponseDTO> listarConFiltros(String numero, String estado, Long programaId){
        return preFacturaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public PreFactura obtenerEntidadPorId(Long id) {
        return preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreFactura no encontrada con id: " + id));
    }
}