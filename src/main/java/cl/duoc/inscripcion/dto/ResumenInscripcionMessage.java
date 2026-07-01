package cl.duoc.inscripcion.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa el resumen de una inscripcion que se envia de forma
 * asincrona hacia la cola RabbitMQ para luego ser consumido y
 * persistido en la tabla resumen_compra.
 */
public class ResumenInscripcionMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long inscripcionId;
    private String nombreEstudiante;
    private String emailEstudiante;
    private LocalDateTime fechaInscripcion;
    private List<String> cursos;
    private Double subtotal;
    private Double iva;
    private Double totalPagar;

    public ResumenInscripcionMessage() {
    }

    public ResumenInscripcionMessage(Long inscripcionId, String nombreEstudiante, String emailEstudiante,
            LocalDateTime fechaInscripcion, List<String> cursos, Double subtotal, Double iva, Double totalPagar) {
        this.inscripcionId = inscripcionId;
        this.nombreEstudiante = nombreEstudiante;
        this.emailEstudiante = emailEstudiante;
        this.fechaInscripcion = fechaInscripcion;
        this.cursos = cursos;
        this.subtotal = subtotal;
        this.iva = iva;
        this.totalPagar = totalPagar;
    }

    public Long getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(Long inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getEmailEstudiante() {
        return emailEstudiante;
    }

    public void setEmailEstudiante(String emailEstudiante) {
        this.emailEstudiante = emailEstudiante;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public List<String> getCursos() {
        return cursos;
    }

    public void setCursos(List<String> cursos) {
        this.cursos = cursos;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(Double totalPagar) {
        this.totalPagar = totalPagar;
    }
}
