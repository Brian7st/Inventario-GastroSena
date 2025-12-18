package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.request.SolicitudGilRequestDTO;
import com.app.Inventario.model.dto.response.SolicitudGilResponseDTO;
import com.app.Inventario.model.entity.SolicitudGil;
import com.app.Inventario.model.enums.EstadoSolicitud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection; // Importación necesaria para flexibilidad
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SolicitudGilMapper {

    private final SolicitudItemMapper solicitudItemMapper;
    private final CuentadanteMapper cuentadanteMapper;

    public SolicitudGilResponseDTO toResponseDto(SolicitudGil entity) {
        if (entity == null) return null;

        SolicitudGilResponseDTO dto = new SolicitudGilResponseDTO();
        dto.setId(entity.getId());
        dto.setCodigoSolicitud(entity.getCodigo());
        dto.setFecha(entity.getFecha());
        dto.setCodigoRegional(entity.getCodigoRegional());
        dto.setNombreRegional(entity.getNombreRegional());
        dto.setCodigoCentroCostos(entity.getCodigoCentroCostos());
        dto.setNombreCentroCostos(entity.getNombreCentroCostos());
        dto.setJefeOficina(entity.getJefeOficina());
        dto.setTipoCuentadante(entity.getTipoCuentadante());
        dto.setDestinoBienes(entity.getDestinoBienes());
        dto.setFichaCaracterizacion(entity.getFichaCaracterizacion());
        dto.setPreFacturaId(entity.getPreFactura() != null ? entity.getPreFactura().getId() : null);
        dto.setEstado(entity.getEstado());

        // Al usar Collection en los mappers hijos, aceptan el Set de la entidad directamente
        if (entity.getCuentadantes() != null) {
            dto.setCuentadantes(cuentadanteMapper.toResponseDtos(entity.getCuentadantes()));
        }

        if (entity.getItems() != null) {
            dto.setItems(solicitudItemMapper.toResponseDtos(entity.getItems()));
        }

        return dto;
    }

    public List<SolicitudGilResponseDTO> toResponseDtos(Collection<SolicitudGil> solicitudes) {
        if (solicitudes == null) return List.of();
        return solicitudes.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public SolicitudGil toEntity(SolicitudGilRequestDTO dto) {
        if (dto == null) return null;

        return SolicitudGil.builder()
                .codigo(dto.getCodigoSolicitud())
                .fecha(dto.getFecha())
                .codigoRegional(dto.getCodigoRegional())
                .nombreRegional(dto.getNombreRegional())
                .codigoCentroCostos(dto.getCodigoCentroCostos())
                .nombreCentroCostos(dto.getNombreCentroCostos())
                .jefeOficina(dto.getJefeOficina())
                .fichaCaracterizacion(dto.getFichaCaracterizacion())
                .tipoCuentadante(dto.getTipoCuentadante())
                .destinoBienes(dto.getDestinoBienes())
                .estado(dto.getEstado() != null ? dto.getEstado() : EstadoSolicitud.PENDIENTE)
                .build();
    }

    public void updateEntityFromDto(SolicitudGil entity, SolicitudGilRequestDTO dto) {
        if (dto == null) return;

        if (dto.getCodigoSolicitud() != null) entity.setCodigo(dto.getCodigoSolicitud());
        if (dto.getJefeOficina() != null) entity.setJefeOficina(dto.getJefeOficina());
        if (dto.getFichaCaracterizacion() != null) entity.setFichaCaracterizacion(dto.getFichaCaracterizacion());
        if (dto.getDestinoBienes() != null) entity.setDestinoBienes(dto.getDestinoBienes());
        if (dto.getTipoCuentadante() != null) entity.setTipoCuentadante(dto.getTipoCuentadante());
    }
}