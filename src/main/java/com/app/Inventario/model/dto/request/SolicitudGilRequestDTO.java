package com.app.Inventario.model.dto.request;

import com.app.Inventario.model.dto.CuentadanteDTO;
import com.app.Inventario.model.dto.response.SolicitudItemRequestDTO;
import com.app.Inventario.model.enums.DestinoBienes;
import com.app.Inventario.model.enums.EstadoSolicitud;
import com.app.Inventario.model.enums.TipoCuentadante;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudGilRequestDTO {

    private Long id;

    @NotBlank
    private String codigoSolicitud;

    @NotNull
    private LocalDate fecha;

    private String codigoRegional;
    private String nombreRegional;
    private String codigoCentroCostos;
    private String nombreCentroCostos;

    private String jefeOficina;

    @NotNull
    private TipoCuentadante tipoCuentadante;

    @NotNull
    private DestinoBienes destinoBienes;

    @NotBlank
    private String fichaCaracterizacion;

    @NotNull
    private Long preFacturaId;

    private Long facturaGlobalId;

    private EstadoSolicitud estado;

    @Valid
    @Size(min = 1)
    private List<CuentadanteDTO> cuentadantes;

    @Valid
    @Size(min = 1)
    private List<SolicitudItemRequestDTO> items;




}
