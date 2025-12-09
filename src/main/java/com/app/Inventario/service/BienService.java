package com.app.Inventario.service;


import com.app.Inventario.mapper.BienMapper;
import com.app.Inventario.model.dto.BienRequestDTO;
import com.app.Inventario.model.dto.BienResponseDTO;
import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.entity.Categoria;
import com.app.Inventario.model.entity.EstadoBien;
import com.app.Inventario.repository.BienRepository;
import com.app.Inventario.repository.CategoriaRepository;
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
    private final BienMapper bienMapper;

    @Transactional
    public BienResponseDTO crearBien(BienRequestDTO request) {
        if (bienRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("Ya existe un bien con el codigo" + request.getCodigo());
        }

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con el ID" + request.getCategoriaId()));

        Bien nuevoBien = bienMapper.toEntity(request, categoria);
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

        bienMapper.updateEntityFromDTO(bienExistente,request,categoria);
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