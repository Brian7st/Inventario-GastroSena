package com.app.Inventario.service;

import com.app.Inventario.mapper.UnidadMedidaMapper;
import com.app.Inventario.model.dto.UnidadMedidaDTO;
import com.app.Inventario.model.entityMaestras.UnidadMedida;
import com.app.Inventario.repository.UnidadMedidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepository;
    private final UnidadMedidaMapper unidadMedidaMapper;

    @Transactional
    public UnidadMedidaDTO crearUnidadMedida(UnidadMedidaDTO request) {
        if (unidadMedidaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe una unidad de medida con el nombre " + request.getNombre());
        }
        if (unidadMedidaRepository.existsByAbreviatura(request.getAbreviatura())) {
            throw new RuntimeException("Ya existe una unidad con la abreviatura " + request.getAbreviatura());
        }

        UnidadMedida nuevaUnidad = unidadMedidaMapper.toUnidadMedidaEntity(request);


        if (nuevaUnidad.getActivo() == null) {
            nuevaUnidad.setActivo(true);
        }

        UnidadMedida unidadGuardada = unidadMedidaRepository.save(nuevaUnidad);
        return unidadMedidaMapper.toUnidadMedidaDTO(unidadGuardada);
    }

    @Transactional(readOnly = true)
    public List<UnidadMedidaDTO> listarTodas() {
        return unidadMedidaRepository
                .findAll().stream()
                .map(unidadMedidaMapper::toUnidadMedidaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UnidadMedidaDTO obtenerPorId(Integer id) {
        UnidadMedida unidad = unidadMedidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unidad de medida no encontrada con ID " + id));

        return unidadMedidaMapper.toUnidadMedidaDTO(unidad);
    }

    @Transactional
    public UnidadMedidaDTO actualizarUnidadMedida(Integer id, UnidadMedidaDTO request) {
        UnidadMedida unidadExistente = unidadMedidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, Unidad no encontrada por ID " + id));


        if (!unidadExistente.getNombre().equals(request.getNombre()) &&
                unidadMedidaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("El nombre " + request.getNombre() + " ya está en uso.");
        }

        if (!unidadExistente.getAbreviatura().equals(request.getAbreviatura()) &&
                unidadMedidaRepository.existsByAbreviatura(request.getAbreviatura())) {
            throw new RuntimeException("La abreviatura " + request.getAbreviatura() + " ya está en uso.");
        }

        unidadMedidaMapper.updateUnidadMedidaFromDTO(unidadExistente, request);

        if (request.getActivo() != null) {
            unidadExistente.setActivo(request.getActivo());
        }

        UnidadMedida unidadActualizada = unidadMedidaRepository.save(unidadExistente);
        return unidadMedidaMapper.toUnidadMedidaDTO(unidadActualizada);
    }

    public void eliminarUnidadMedida(Integer id) {
        UnidadMedida unidad = unidadMedidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unidad de medida no encontrada ID " + id));

        unidad.setActivo(false);
        unidadMedidaRepository.save(unidad);
    }
}