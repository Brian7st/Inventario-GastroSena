package com.app.Inventario.mapper;

import com.app.Inventario.model.dto.SolicitudGilResponseDTO;
import com.app.Inventario.model.entity.Cuentadante;
import com.app.Inventario.model.entity.SolicitudGil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SolicitudGilMapper {

    @Autowired
    private SolicitudItemMapper solicitudItemMapper;
    @Autowired
    private CuentadanteMapper cuentadanteMapper;



    public static SolicitudGilResponseDTO toResponseDto(SolicitudGil solicitudGil){
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
        dto.setJefeOficina(solicitudGil.getJefeOficina());
        dto.setTipoCuentadante(solicitudGil.getTipoCuentadante());
        dto.setDestinoBienes(solicitudGil.getDestinoBienes());
        dto.setFichaCaracterizacion(solicitudGil.getFichaCaracterizacion());
        dto.setPreFacturaId(solicitudGil.getPreFactura() != null ? solicitudGil.getPreFactura().getId() : null);
        dto.setFacturaGlobalId(solicitudGil.getFacturaGlobal() != null ? solicitudGil.getFacturaGlobal().getId() : null);
        dto.setEstado(solicitudGil.getEstado());
        dto.setCuentadantes(cuentadanteMapper.toResponseDtos(solicitudGil.getCuentadantes()));
        dto.setItems(solicitudItemMapper.toResponseDtos(solicitudGil.getItems()));





        return dto;
    }

    public List<SolicitudGilResponseDTO> toResponseDtos (List<SolicitudGil> solicitudes){
        if (solicitudes == null){
            return null;
        }else{
            return solicitudes.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());
        }



}}
