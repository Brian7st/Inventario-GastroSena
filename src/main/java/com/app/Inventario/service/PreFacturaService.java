
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
                .orElseThrow(() -> new RuntimeException("Programa de Formación no encontrado con ID: " + request.getProgramaId()));

        PreFactura nuevaPreFactura = preFacturaMapper.toEntity(request, programa);
        nuevaPreFactura.setFecha(LocalDateTime.now());
        nuevaPreFactura.setEstado(EstadoPreFactura.PENDIENTE);
        nuevaPreFactura.setDetalles(new ArrayList<>());

        PreFactura preFacturaGuardada = preFacturaRepository.save(nuevaPreFactura);

        BigDecimal totalGeneralCalculado = procesarDetalles(request.getDetalles(), preFacturaGuardada);

        preFacturaGuardada.setTotalPrefactura(totalGeneralCalculado);
        preFacturaRepository.save(preFacturaGuardada);

        return preFacturaMapper.toResponseDtoPreFactura(preFacturaGuardada);
    }


    @Transactional(readOnly = true)
    public List<PreFacturaResponseDTO> listarTodos(){
        return preFacturaRepository
                .findAll().stream()
                .map(preFacturaMapper::toResponseDtoPreFactura)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PreFacturaResponseDTO obtenerPorId(long id){
        PreFactura preFactura = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreFactura no encontrada con ID: " + id));

        return preFacturaMapper.toResponseDtoPreFactura(preFactura);
    }


    @Transactional
    public PreFacturaResponseDTO actualizarPreFactura (long id, PreFacturaRequestDTO request){
        PreFactura preFacturaExistente = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, PreFactura no encontrada por ID: " + id));


        if (preFacturaExistente.getEstado() != EstadoPreFactura.PENDIENTE) {
            throw new RuntimeException("Solo se pueden modificar PreFacturas en estado PENDIENTE. Estado actual: " + preFacturaExistente.getEstado());
        }


        if(!preFacturaExistente.getNumero().equals(request.getNumero()) && preFacturaRepository.existsByNumero(request.getNumero())){
            throw new RuntimeException("El número de PreFactura " + request.getNumero() + " ya está en uso por otra prefactura.");
        }


        preFacturaDetalleRepository.deleteAll(preFacturaExistente.getDetalles());
        preFacturaExistente.getDetalles().clear();


        ProgramaFormacion programa = programaFormacionRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa de Formación no encontrado con ID: " + request.getProgramaId()));

        preFacturaMapper.updateEntityFromDTO(preFacturaExistente, request, programa);



        BigDecimal nuevoTotal = procesarDetalles(request.getDetalles(), preFacturaExistente);

        preFacturaExistente.setTotalPrefactura(nuevoTotal);

        PreFactura preFacturaActualizada = preFacturaRepository.save(preFacturaExistente);
        return  preFacturaMapper.toResponseDtoPreFactura(preFacturaActualizada);
    }


    @Transactional
    public void anularPreFactura(long id){
        PreFactura preFactura = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreFactura no encontrada ID " + id));

        if (preFactura.getEstado() == EstadoPreFactura.ANULADA) {
            throw new RuntimeException("La PreFactura ID " + id + " ya se encuentra ANULADA.");
        }


        preFactura.setEstado(EstadoPreFactura.ANULADA);

        preFacturaRepository.save(preFactura);
    }


    @Transactional
    public PreFacturaResponseDTO validarPreFactura(long id) {
        PreFactura preFactura = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreFactura no encontrada ID " + id));

        if (preFactura.getEstado() == EstadoPreFactura.ANULADA) {
            throw new RuntimeException("No se puede validar una PreFactura ANULADA.");
        }

        if (preFactura.getEstado() == EstadoPreFactura.VALIDADA) {
            return preFacturaMapper.toResponseDtoPreFactura(preFactura);
        }

        preFactura.setEstado(EstadoPreFactura.VALIDADA);
        PreFactura preFacturaValidada = preFacturaRepository.save(preFactura);

        return preFacturaMapper.toResponseDtoPreFactura(preFacturaValidada);
    }



    private BigDecimal procesarDetalles(List<PreFacturaDetalleRequestDTO> detallesRequest, PreFactura preFactura) {
        BigDecimal totalGeneralCalculado = BigDecimal.ZERO;

        for (PreFacturaDetalleRequestDTO detalleRequest : detallesRequest) {

            Bien bien = bienRepository.findById(detalleRequest.getBienId())
                    .orElseThrow(() -> new RuntimeException("Bien no encontrado con ID: " + detalleRequest.getBienId() + " en el detalle."));


            PreFacturaDetalle detalleEntity = preFacturaDetalleMapper.toEntity(
                    detalleRequest,
                    bien,
                    preFactura
            );


            BigDecimal totalLinea = detalleRequest.getCantidad().multiply(detalleRequest.getPrecioAdjudicado());
            totalGeneralCalculado = totalGeneralCalculado.add(totalLinea);

            preFactura.getDetalles().add(detalleEntity);
        }
        return totalGeneralCalculado;
    }


    public PreFactura obtenerEntidadPorId(Long id) {
        return preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreFactura no encontrada con id: " + id));
    }
    @Transactional(readOnly = true)
    public List<PreFacturaResponseDTO> listarConFiltros(String numero, String estado, Long programaId){

        List<PreFactura> preFacturas = preFacturaRepository.findAll();


        return preFacturas.stream()
                .map(preFacturaMapper::toResponseDtoPreFactura)
                .collect(Collectors.toList());
    }
}