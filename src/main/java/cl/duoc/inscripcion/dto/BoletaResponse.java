package cl.duoc.inscripcion.dto;

import java.time.LocalDateTime;
import java.util.List;

import cl.duoc.inscripcion.entities.Curso;
import cl.duoc.inscripcion.entities.Inscripcion;

public class BoletaResponse {

    private Long numeroBoleta;
    private String nombreEstudiante;
    private String emailEstudiante;
    private LocalDateTime fechaInscripcion;
    private List<Curso> cursos;
    private Double subtotal;
    private Double iva;
    private Double totalPagar;
    private String mensaje;

    public BoletaResponse() {
    }

    public static BoletaResponse desdeInscripcion(Inscripcion ins) {
        BoletaResponse b = new BoletaResponse();
        b.numeroBoleta = ins.getId();
        b.nombreEstudiante = ins.getNombreEstudiante();
        b.emailEstudiante = ins.getEmailEstudiante();
        b.fechaInscripcion = ins.getFechaInscripcion();
        b.cursos = ins.getCursos();
        double subtotal = ins.getCursos().stream().mapToDouble(Curso::getCosto).sum();
        b.subtotal = subtotal;
        b.iva = Math.round(subtotal * 0.19 * 100.0) / 100.0;
        b.totalPagar = ins.getTotalPagar();
        b.mensaje = "Inscripcion registrada exitosamente";
        return b;
    }

    public Long getNumeroBoleta() {
        return numeroBoleta;
    }

    public void setNumeroBoleta(Long numeroBoleta) {
        this.numeroBoleta = numeroBoleta;
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

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
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

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
