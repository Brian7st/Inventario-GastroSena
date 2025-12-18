    package com.app.Inventario.model.dto.response;


    import com.app.Inventario.model.entity.ProgramaFormacion;
    import com.app.Inventario.model.enums.EstadoPreFactura;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor

    public class PreFacturaResponseDTO {
        private Long id;
        private String numero;
        private LocalDateTime fecha;

        private String instructorNombre;
        private String instructorIdentificacion;

        private EstadoPreFactura estado;
        private BigDecimal totalGeneral;


        private Long programaId;
        private String programaNombre;

        private List<PreFacturaDetalleResponseDTO> detalles;
    }
