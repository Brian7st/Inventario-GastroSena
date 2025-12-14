package com.app.Inventario.model.dto;

import com.app.Inventario.model.enums.DestinoBienes;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.model.enums.TipoCuentadante;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SolicitudGilResponseDTO {

    private Long id;
    private String codigoSolicitud;
    private LocalDate fecha;

    private Integer codigoRegional;
    private String nombreRegional;
    private String codigoCentroCostos;

    private String jefeOficina;

    private TipoCuentadante tipoCuentadante;
    private DestinoBienes destinoBienes;
    private String fichaCaracterizacion;

    private Long preFacturaId;
    private Long facturaGlobalId;

    private EstadoSolicitud estado;


    private List<CuentadanteDTO> cuentadantes;
    private List<SolicitudItemResponseDTO> items;
}
