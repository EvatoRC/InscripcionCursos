package cl.duoc.inscripcion.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
 * Tabla nueva donde el consumidor de la cola RabbitMQ persiste el
 * resumen de compra de cada inscripcion (requisito de la actividad
 * Semana 7: "guardara el resumen de compra en una nueva tabla de una
 * Base de datos").
 */
@Entity
@Table(name = "resumen_compra")
public class ResumenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long inscripcionId;

    @Column(nullable = false)
    private String nombreEstudiante;

    @Column(nullable = false)
    private String emailEstudiante;

    @Column(nullable = false)
    private LocalDateTime fechaInscripcion;

    @ElementCollection
    @CollectionTable(name = "resumen_compra_cursos", joinColumns = @JoinColumn(name = "resumen_compra_id"))
    @Column(name = "curso_nombre")
    private List<String> cursos = new ArrayList<>();

    @Column(nullable = false)
    private Double subtotal;

    @Column(nullable = false)
    private Double iva;

    @Column(nullable = false)
    private Double totalPagar;

    @Column(nullable = false)
    private LocalDateTime fechaProcesado;

    public ResumenCompra() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getFechaProcesado() {
        return fechaProcesado;
    }

    public void setFechaProcesado(LocalDateTime fechaProcesado) {
        this.fechaProcesado = fechaProcesado;
    }
}
