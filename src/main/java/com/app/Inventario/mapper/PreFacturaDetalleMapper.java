package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.request.PreFacturaDetalleRequestDTO;
import com.app.Inventario.model.dto.response.PreFacturaDetalleResponseDTO;
import com.app.Inventario.model.entity.Bien;
import com.app.Inventario.model.entity.PreFactura;
import com.app.Inventario.model.entity.PreFacturaDetalle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PreFacturaDetalleMapper {


    public PreFacturaDetalleResponseDTO toResponseDto(PreFacturaDetalle detalle) {
        if (detalle == null) {
            return null;
        }

        PreFacturaDetalleResponseDTO dto = new PreFacturaDetalleResponseDTO();

        dto.setId(detalle.getId());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioAdjudicado(detalle.getPrecioAdjudicado());

        dto.setTotalLinea(detalle.getTotalLinea());

        if (detalle.getBien() != null) {
            dto.setBienId(detalle.getBien().getId());
            dto.setNombreBien(detalle.getBien().getNombre());
            dto.setCodigoBien(detalle.getBien().getCodigo());
        }

        return dto;
    }


    public PreFacturaDetalle toEntity(
            PreFacturaDetalleRequestDTO dto,
            Bien bien,
            PreFactura preFactura) {

        if (dto == null) {
            return null;
        }

        PreFacturaDetalle detalle = new PreFacturaDetalle();

        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioAdjudicado(dto.getPrecioAdjudicado());


        detalle.setBien(bien);
        detalle.setPreFactura(preFactura);

        BigDecimal totalLinea = dto.getCantidad().multiply(dto.getPrecioAdjudicado());
        detalle.setTotalLinea(totalLinea);

        return detalle;
    }

}