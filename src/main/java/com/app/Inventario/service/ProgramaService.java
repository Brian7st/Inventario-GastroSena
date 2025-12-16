package com.app.Inventario.service;

import com.app.Inventario.mapper.ProgramaMapper;
import com.app.Inventario.model.dto.request.ProgramaRequestDTO;
import com.app.Inventario.model.dto.response.ProgramaResponseDTO;
import com.app.Inventario.model.entity.ProgramaFormacion;
import com.app.Inventario.model.enums.EstadoPrograma;
import com.app.Inventario.repository.ProgramaFormacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramaService {

    private final ProgramaFormacionRepository programaFormacionRepository;
    private final ProgramaMapper programaMapper; // Inyección del Mapper

    @Transactional
    public ProgramaResponseDTO crearPrograma(ProgramaRequestDTO request) {

        if (programaFormacionRepository.existsByCodigoFicha(request.getCodigoFicha())) {
            throw new RuntimeException("Ya existe un Programa de Formación con el código de ficha: " + request.getCodigoFicha());
        }

        ProgramaFormacion nuevoPrograma = programaMapper.toEntity(request);


        if (nuevoPrograma.getEstadoPrograma() == null) {
            nuevoPrograma.setEstadoPrograma(EstadoPrograma.ACTIVO);
        }

        ProgramaFormacion programaGuardado = programaFormacionRepository.save(nuevoPrograma);

        return programaMapper.toResponseDto(programaGuardado);
    }

    @Transactional(readOnly = true)
    public List<ProgramaResponseDTO> listarTodos(){
        return programaFormacionRepository
                .findAll().stream()
                .map(programaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProgramaResponseDTO obtenerPorId(long id){
        ProgramaFormacion programa = programaFormacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programa de Formación no encontrado con ID: " + id));

        return programaMapper.toResponseDto(programa);
    }

    @Transactional
    public ProgramaResponseDTO actualizarPrograma (long id, ProgramaRequestDTO request){

        ProgramaFormacion programaExistente = programaFormacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, Programa no encontrado por ID: " + id));

        if(!programaExistente.getCodigoFicha().equals(request.getCodigoFicha()) && programaFormacionRepository.existsByCodigoFicha(request.getCodigoFicha())){
            throw new RuntimeException("El código de ficha " + request.getCodigoFicha() + " ya está en uso por otro programa.");
        }

        programaMapper.updateEntityFromDTO(programaExistente, request);

        ProgramaFormacion programaActualizado = programaFormacionRepository.save(programaExistente);

        return  programaMapper.toResponseDto(programaActualizado);
    }


    @Transactional
    public void eliminarPrograma(long id){
        ProgramaFormacion programa = programaFormacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programa de Formación no encontrado ID " + id));


        programa.setEstadoPrograma(EstadoPrograma.INACTIVO);
        programaFormacionRepository.save(programa);
    }
}