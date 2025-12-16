package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.request.BienRequestDTO;
import com.app.Inventario.model.dto.response.BienResponseDTO;
import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.entityMaestras.Categoria;
import com.app.Inventario.model.entityMaestras.Impuesto;
import com.app.Inventario.model.entityMaestras.UnidadMedida;
import com.app.Inventario.model.enums.EstadoBien;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BienMapper {


    public BienResponseDTO toResponseDto(Bien bien) {
        if (bien == null) {
            return null;
        }

        // Cálculo auxiliar para el Precio Final (Visualización)
        // Precio Base * (1 + (Porcentaje / 100))
        BigDecimal precioFinal = BigDecimal.ZERO;
        BigDecimal porcentajeImpuesto = BigDecimal.ZERO;

        if (bien.getImpuesto() != null && bien.getValorUnitario() != null) {
            porcentajeImpuesto = bien.getImpuesto().getPorcentaje();
            // Convertimos 19.00 a 0.19
            BigDecimal factor = porcentajeImpuesto.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            BigDecimal montoImpuesto = bien.getValorUnitario().multiply(factor);
            precioFinal = bien.getValorUnitario().add(montoImpuesto);
        }

        return BienResponseDTO.builder()
                .idBien(bien.getId())
                .codigo(bien.getCodigo())
                .codAlmacen(bien.getCodAlmacen())
                .nombre(bien.getNombre())
                .descripcion(bien.getDescripcion())

                .categoriaId(bien.getCategoria() != null ? bien.getCategoria().getId() : null)
                .categoriaNombre(bien.getCategoria() != null ? bien.getCategoria().getNombre() : null)

                .unidadId(bien.getUnidadMedida() != null ? bien.getUnidadMedida().getIdUnidad() : null)
                .unidadNombre(bien.getUnidadMedida() != null ? bien.getUnidadMedida().getAbreviatura() : null)

                .impuestoId(bien.getImpuesto() != null ? bien.getImpuesto().getId() : null)
                .impuestoNombre(bien.getImpuesto() != null ? bien.getImpuesto().getNombre() : null)
                .impuestoPorcentaje(porcentajeImpuesto)


                .valorUnitario(bien.getValorUnitario())
                .precioVentaFinal(precioFinal)


                .stockActual(bien.getStockActual())
                .valorTotalStock(bien.getValorTotalStock())
                .stockMinimo(bien.getStockMinimo())
                .estado(bien.getEstado())
                .activo(bien.getActivo())
                .build();
    }


    public Bien toEntity(BienRequestDTO dto, Categoria categoria, UnidadMedida unidad, Impuesto impuesto) {
        if (dto == null) {
            return null;
        }

        return Bien.builder()
                .codigo(dto.getCodigo())
                .codAlmacen(dto.getCodAlmacen())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())

                .categoria(categoria)
                .unidadMedida(unidad)
                .impuesto(impuesto)

                .valorUnitario(dto.getValorUnitario())
                .stockMinimo(dto.getStockMinimo())

                .stockActual(BigDecimal.ZERO)
                .estado(dto.getEstado()!= null ? dto.getEstado() : EstadoBien.SIN_STOCK)
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    public void updateEntityFromDTO(Bien bienExistente, BienRequestDTO dto,
                                    Categoria nuevaCategoria,
                                    UnidadMedida nuevaUnidad,
                                    Impuesto nuevoImpuesto) {

        bienExistente.setCodigo(dto.getCodigo());
        bienExistente.setCodAlmacen(dto.getCodAlmacen());
        bienExistente.setNombre(dto.getNombre());
        bienExistente.setDescripcion(dto.getDescripcion());

        // Actualizamos relaciones
        if (nuevaCategoria != null) bienExistente.setCategoria(nuevaCategoria);
        if (nuevaUnidad != null)    bienExistente.setUnidadMedida(nuevaUnidad);
        if (nuevoImpuesto != null)  bienExistente.setImpuesto(nuevoImpuesto);

        bienExistente.setValorUnitario(dto.getValorUnitario());
        bienExistente.setStockMinimo(dto.getStockMinimo());


        if (dto.getEstado() != null) {
            bienExistente.setEstado(dto.getEstado());
        }
        if (dto.getActivo() != null) {
            bienExistente.setActivo(dto.getActivo());
        }
    }
}