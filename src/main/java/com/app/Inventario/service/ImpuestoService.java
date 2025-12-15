package com.app.Inventario.service;

import com.app.Inventario.mapper.ImpuestoMapper;
import com.app.Inventario.model.dto.ImpuestoDTO;
import com.app.Inventario.model.entityMaestras.Impuesto;
import com.app.Inventario.repository.ImpuestoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImpuestoService {

    private final ImpuestoRepository impuestoRepository;
    private final ImpuestoMapper impuestoMapper;

    @Transactional
    public ImpuestoDTO crearImpuesto(ImpuestoDTO request) {
        if (impuestoRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe un impuesto con el nombre " + request.getNombre());
        }

        Impuesto nuevoImpuesto = impuestoMapper.toImpuestoEntity(request);

        // Asegurar valor por defecto si es nulo
        if (nuevoImpuesto.getActivo() == null) {
            nuevoImpuesto.setActivo(true);
        }

        Impuesto impuestoGuardado = impuestoRepository.save(nuevoImpuesto);

        return impuestoMapper.toImpuestoDTO(impuestoGuardado);
    }

    @Transactional(readOnly = true)
    public List<ImpuestoDTO> listarTodos() {
        return impuestoRepository
                .findAll().stream()
                .map(impuestoMapper::toImpuestoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ImpuestoDTO obtenerPorId(Integer id) {
        Impuesto impuesto = impuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impuesto no encontrado con ID " + id));

        return impuestoMapper.toImpuestoDTO(impuesto);
    }

    @Transactional
    public ImpuestoDTO actualizarImpuesto(Integer id, ImpuestoDTO request) {

        Impuesto impuestoExistente = impuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, Impuesto no encontrado por ID " + id));

        // Validar nombre duplicado solo si cambió
        if (!impuestoExistente.getNombre().equals(request.getNombre()) &&
                impuestoRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("El nombre " + request.getNombre() + " ya está en uso por otro impuesto.");
        }

        // Mapeo de campos básicos
        impuestoMapper.updateImpuestoFromDTO(impuestoExistente, request);

        // NOTA: Tu mapper original no actualizaba el porcentaje, lo agregamos aquí manualmente para asegurar integridad
        if(request.getPorcentaje() != null) {
            impuestoExistente.setPorcentaje(request.getPorcentaje());
        }

        Impuesto impuestoActualizado = impuestoRepository.save(impuestoExistente);

        return impuestoMapper.toImpuestoDTO(impuestoActualizado);
    }

    public void eliminarImpuesto(Integer id) {
        Impuesto impuesto = impuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impuesto no encontrado ID " + id));

        // Eliminación lógica (Soft Delete)
        impuesto.setActivo(false);
        impuestoRepository.save(impuesto);
    }
}