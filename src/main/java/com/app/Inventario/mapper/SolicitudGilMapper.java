package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.request.SolicitudGilRequestDTO;
import com.app.Inventario.model.dto.response.SolicitudGilResponseDTO;
import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.model.enums.EstadoSolicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class SolicitudGilMapper {


    private final SolicitudItemMapper solicitudItemMapper;
    private final CuentadanteMapper cuentadanteMapper;


    public SolicitudGilResponseDTO toResponseDto(SolicitudGil solicitudGil){
        if (solicitudGil == null){
            return null;
        }

        SolicitudGilResponseDTO dto = new SolicitudGilResponseDTO();

        dto.setId(solicitudGil.getId());
        dto.setCodigoSolicitud(solicitudGil.getCodigo());
        dto.setFecha(solicitudGil.getFecha());
        dto.setCodigoRegional(solicitudGil.getCodigoRegional());
        dto.setNombreRegional(solicitudGil.getNombreRegional());
        dto.setCodigoCentroCostos(solicitudGil.getCodigoCentroCostos());
        dto.setNombreCentroCostos(solicitudGil.getNombreCentroCostos());
        dto.setJefeOficina(solicitudGil.getJefeOficina());
        dto.setTipoCuentadante(solicitudGil.getTipoCuentadante());
        dto.setDestinoBienes(solicitudGil.getDestinoBienes());
        dto.setFichaCaracterizacion(solicitudGil.getFichaCaracterizacion());
        dto.setPreFacturaId(solicitudGil.getPreFactura() != null ? solicitudGil.getPreFactura().getId() : null);
        dto.setEstado(solicitudGil.getEstado());
        dto.setCuentadantes(cuentadanteMapper.toResponseDtos(solicitudGil.getCuentadantes()));
        dto.setItems(solicitudItemMapper.toResponseDtos(solicitudGil.getItems()));

        return dto;
    }

    // --- Mapeo de Lista a Response DTOs ---
    public List<SolicitudGilResponseDTO> toResponseDtos (List<SolicitudGil> solicitudes){
        if (solicitudes == null){
            return null;
        }
        return solicitudes.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


    public SolicitudGil toEntity(SolicitudGilRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return SolicitudGil.builder()
                .codigo(dto.getCodigoSolicitud())
                .fecha(dto.getFecha()) // Importante: persistir la fecha del DTO
                .jefeOficina(dto.getJefeOficina())
                .fichaCaracterizacion(dto.getFichaCaracterizacion())
                .tipoCuentadante(dto.getTipoCuentadante())
                .destinoBienes(dto.getDestinoBienes())
                .estado(dto.getEstado() != null ? dto.getEstado() : EstadoSolicitud.PENDIENTE)
                .build();
    }


    public void updateEntityFromDto(SolicitudGil solicitudExistente, SolicitudGilRequestDTO dto) {


        if (dto.getCodigoSolicitud() != null) {
            solicitudExistente.setCodigo(dto.getCodigoSolicitud());
        }


        if (dto.getJefeOficina() != null) {
            solicitudExistente.setJefeOficina(dto.getJefeOficina());
        }
        if (dto.getFichaCaracterizacion() != null) {
            solicitudExistente.setFichaCaracterizacion(dto.getFichaCaracterizacion());
        }


    }
}