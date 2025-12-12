package com.app.Inventario.service;

import com.app.Inventario.mapper.PreFacturaMapper;
import com.app.Inventario.model.dto.PreFacturaRequestDTO;
import com.app.Inventario.model.dto.PreFacturaResponseDTO;
import com.app.Inventario.model.entity.PreFactura;
import com.app.Inventario.model.entity.ProgramaFormacion;
import com.app.Inventario.model.enums.EstadoPreFactura;
import com.app.Inventario.repository.PreFacturaRepository;
import com.app.Inventario.repository.ProgramaFormacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreFacturaService {

    private final PreFacturaRepository preFacturaRepository;
    private final ProgramaFormacionRepository programaFormacionRepository;
    private final PreFacturaMapper preFacturaMapper;

    @Transactional
    public PreFacturaResponseDTO crearPreFactura(PreFacturaRequestDTO request) {

        if (preFacturaRepository.existsByNumero(request.getNumero())) {
            throw new RuntimeException("Ya existe una PreFactura con el número: " + request.getNumero());
        }

        ProgramaFormacion programa = programaFormacionRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa de Formación no encontrado con ID: " + request.getProgramaId()));

        PreFactura nuevaPreFactura = preFacturaMapper.toEntity(request, programa);
        nuevaPreFactura.setFecha(LocalDateTime.now());
        nuevaPreFactura.setEstado(EstadoPreFactura.PENDIENTE); // Estado inicial
        PreFactura preFacturaGuardada = preFacturaRepository.save(nuevaPreFactura);
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

        if(!preFacturaExistente.getNumero().equals(request.getNumero()) && preFacturaRepository.existsByNumero(request.getNumero())){
            throw new RuntimeException("El número de PreFactura " + request.getNumero() + " ya está en uso por otra prefactura.");
        }

        ProgramaFormacion programa = programaFormacionRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new RuntimeException("Programa de Formación no encontrado con ID: " + request.getProgramaId()));

        preFacturaMapper.updateEntityFromDTO(preFacturaExistente, request, programa);

        PreFactura preFacturaActualizada = preFacturaRepository.save(preFacturaExistente);
        return  preFacturaMapper.toResponseDtoPreFactura(preFacturaActualizada);
    }


    @Transactional
    public void anularPreFactura(long id){
        PreFactura preFactura = preFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreFactura no encontrada ID " + id));

        if(preFactura.getEstado() == EstadoPreFactura.ANULADA){
            throw new RuntimeException("La PreFactura ya se encuentra ANULADA.");
        }
        preFactura.setEstado(EstadoPreFactura.ANULADA);
        preFacturaRepository.save(preFactura);
    }
}