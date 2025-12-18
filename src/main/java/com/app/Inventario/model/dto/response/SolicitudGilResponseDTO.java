package com.app.Inventario.model.dto.response;

import com.app.Inventario.model.dto.CuentadanteDTO;
import com.app.Inventario.model.enums.DestinoBienes;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.model.enums.TipoCuentadante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudGilResponseDTO {

    private Long id;
    private String codigoSolicitud;
    private LocalDate fecha;

    private String codigoRegional;
    private String nombreRegional;
    private String codigoCentroCostos;
    private String nombreCentroCostos;
    private String jefeOficina;

    private TipoCuentadante tipoCuentadante;
    private DestinoBienes destinoBienes;
    private String fichaCaracterizacion;

    private Long preFacturaId;


    private EstadoSolicitud estado;


    private List<CuentadanteDTO> cuentadantes;
    private List<SolicitudItemResponseDTO> items;
}
