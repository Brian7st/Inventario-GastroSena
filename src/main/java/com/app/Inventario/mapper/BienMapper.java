package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.BienRequestDTO;
import com.app.Inventario.model.dto.BienResponseDTO;
import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.entity.Categoria;
import com.app.Inventario.model.entity.EstadoBien;
import org.springframework.stereotype.Component;

@Component
public class BienMapper {

    public BienResponseDTO toResponseDto(Bien bien){
        if(bien == null){
            return null;
        }

        BienResponseDTO dto = new BienResponseDTO();

        dto.setId(bien.getId());
        dto.setCodigo(bien.getCodigo());
        dto.setNombre(bien.getNombre());
        dto.setUnidadMedida(bien.getUnidadMedida());
        dto.setValorUnitario(bien.getValorUnitario());
        dto.setPorcentajeIva(bien.getPorcentajeIva());
        dto.setValorConIva(bien.getValorConIva()); // calculado de la BD
        dto.setStockActual(bien.getStockActual());
        dto.setStockMinimo(bien.getStockMinimo());
        dto.setEstado(bien.getEstado());

        if(bien.getCategoria() != null){
            dto.setCategoriaId(bien.getCategoria().getId());
            dto.setCategoriaNombre(bien.getCategoria().getNombre());
        }

        return dto;
    }

    public Bien toEntity(BienRequestDTO dto, Categoria categoria){

        if(dto == null){
            return null;
        }

        Bien bien = new Bien();

        bien.setCodigo(dto.getCodigo());
        bien.setNombre(dto.getNombre());
        bien.setUnidadMedida(dto.getUnidadMedida());
        bien.setValorUnitario(dto.getValorUnitario());
        bien.setPorcentajeIva(dto.getPorcentajeIva());
        bien.setStockActual(dto.getStockActual());
        bien.setStockMinimo(dto.getStockMinimo());
        bien.setCategoria(categoria);

        if(dto.getEstado() != null){
            bien.setEstado(dto.getEstado());
        }else{
            bien.setEstado(EstadoBien.DISPONIBLE);
        }

        return bien;

    }

    public void updateEntityFromDTO(Bien bienExistente, BienRequestDTO dto, Categoria nuevaCategoria){
        bienExistente.setCodigo(dto.getCodigo());
        bienExistente.setNombre(dto.getNombre());
        bienExistente.setUnidadMedida(dto.getUnidadMedida());
        bienExistente.setValorUnitario(dto.getValorUnitario());
        bienExistente.setPorcentajeIva(dto.getPorcentajeIva());
        bienExistente.setStockActual(dto.getStockActual());
        bienExistente.setStockMinimo(dto.getStockMinimo());

        bienExistente.setCategoria(nuevaCategoria);

        if (dto.getEstado() != null) {
            bienExistente.setEstado(dto.getEstado());
        }
    }

}
