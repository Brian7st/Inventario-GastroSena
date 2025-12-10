package com.app.Inventario.model.dto;

import com.app.Inventario.model.enums.TipoCuentadante;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class SolicitudGilDTO {

    private Long id;

    private Long numeroPreFactura;

    private LocalDate fechaActual;

    private String programa;

    @NotBlank(message = "identificacion requerida")
    @Size(max = 20)
    private String identificacion;

    @NotBlank(message = "el codigo de area es obligatorio")
    private String codigoArea;


    @NotBlank(message = "el nombre del cuentadante es requerido")
    private String nombreCuentadante;

    @NotBlank(message = "la identificacion del cuentadante es requerido")
    private  String identificacionCuentadante;

    @Valid
    @NotNull(message = "la solicitud debe tener almenos un bien ")
    @Size(min = 1, message = "la solicitud tiene que tener 1 o mas items   ")

    @NotNull(message = "El tipo de cuentadante es requerido (UNIPERSONAL/MULTIPLE)")
    private TipoCuentadante tipoCuentadante;

    private List<SolicitudItemDTO> items;




}
