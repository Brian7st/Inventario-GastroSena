package com.app.Inventario.service;


import com.app.Inventario.mapper.BienMapper;
import com.app.Inventario.model.dto.request.BienRequestDTO;
import com.app.Inventario.model.dto.response.BienResponseDTO;
import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.entityMaestras.Categoria;
import com.app.Inventario.model.entityMaestras.Impuesto;
import com.app.Inventario.model.entityMaestras.UnidadMedida;
import com.app.Inventario.model.enums.EstadoBien;
import com.app.Inventario.repository.BienRepository;
import com.app.Inventario.repository.CategoriaRepository;
import com.app.Inventario.repository.ImpuestoRepository;
import com.app.Inventario.repository.UnidadMedidaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BienService {

    private final BienRepository bienRepository;
    private final CategoriaRepository categoriaRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final ImpuestoRepository impuestoRepository;

    private final BienMapper bienMapper;

    @Transactional
    public BienResponseDTO crearBien(BienRequestDTO request) {
        if (bienRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("Ya existe un bien con el codigo" + request.getCodigo());
        }

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con el ID" + request.getCategoriaId()));

        UnidadMedida unidadMedida = unidadMedidaRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new RuntimeException("Unidad de medida no encontrada con el id" + request.getUnidadId()));

        Impuesto impuesto = impuestoRepository.findById(request.getImpuestoId())
                .orElseThrow(()-> new RuntimeException("Impuesto no encontrado con el id" + request.getImpuestoId()));

        Bien nuevoBien = bienMapper.toEntity(request,categoria,unidadMedida,impuesto);
        Bien bienGuardado = bienRepository.save(nuevoBien);

        return bienMapper.toResponseDto(bienGuardado);
    }

    @Transactional(readOnly = true)
    public List<BienResponseDTO> listarTodos(){
        return bienRepository
                .findAll().stream()
                .map(bienMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BienResponseDTO obtenerPorId(long id){
        Bien bien = bienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bien no encontrado con ID" + id));

        return bienMapper.toResponseDto(bien);
    }

    @Transactional
    public BienResponseDTO actualizarBien (long id, BienRequestDTO request){

        Bien bienExistente = bienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, Bien no encontrado por ID" + id));

        if(!bienExistente.getCodigo().equals(request.getCodigo()) && bienRepository.existsByCodigo(request.getCodigo())){
            throw new RuntimeException("El código " + request.getCodigo() + " ya está en uso por otro producto.");
        }

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada ID" + request.getCategoriaId()));

        UnidadMedida unidadMedida = unidadMedidaRepository.findById(request.getUnidadId())
                .orElseThrow(() -> new RuntimeException("Unidad de medida no encontrada con el id" + request.getUnidadId()));

        Impuesto impuesto = impuestoRepository.findById(request.getImpuestoId())
                .orElseThrow(()-> new RuntimeException("Impuesto no encontrado con el id" + request.getImpuestoId()));

        bienMapper.updateEntityFromDTO(bienExistente,request,categoria,unidadMedida,impuesto);
        Bien bienActualizado = bienRepository.save(bienExistente);

        return  bienMapper.toResponseDto(bienActualizado);
    }

    public void eliminarBien(long id){
        Bien bien = bienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bien no encontrado ID " + id));
        bien.setEstado(EstadoBien.ELIMINADO);
        bienRepository.save(bien);
    }
}