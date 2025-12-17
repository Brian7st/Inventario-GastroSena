package com.app.Inventario.service;

import com.app.Inventario.mapper.CatalogoMapper;
import com.app.Inventario.model.dto.CategoriaDTO;
import com.app.Inventario.model.entityMaestras.Categoria;
import com.app.Inventario.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CatalogoMapper catalogoMapper;

    @Transactional
    public CategoriaDTO crearCategoria(CategoriaDTO request) {
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre " + request.getNombre());
        }

        Categoria nuevaCategoria = catalogoMapper.toCategoriaEntity(request);


        if (nuevaCategoria.getActivo() == null) {
            nuevaCategoria.setActivo(true);
        }

        Categoria categoriaGuardada = categoriaRepository.save(nuevaCategoria);

        return catalogoMapper.toCategoriaDTO(categoriaGuardada);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository
                .findAll().stream()
                .map(catalogoMapper::toCategoriaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaDTO obtenerPorId(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID " + id));

        return catalogoMapper.toCategoriaDTO(categoria);
    }

    @Transactional
    public CategoriaDTO actualizarCategoria(Integer id, CategoriaDTO request) {

        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, Categoría no encontrada por ID " + id));


        if (!categoriaExistente.getNombre().equals(request.getNombre()) &&
                categoriaRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("El nombre " + request.getNombre() + " ya está en uso por otra categoría.");
        }


        catalogoMapper.updateCategoriaFromDto(categoriaExistente, request);

        Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);

        return catalogoMapper.toCategoriaDTO(categoriaActualizada);
    }


    public void eliminarCategoria(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada ID " + id));

        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }
}