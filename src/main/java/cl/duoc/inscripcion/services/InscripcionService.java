package cl.duoc.inscripcion.services;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.inscripcion.dto.InscripcionRequest;
import cl.duoc.inscripcion.dto.ResumenInscripcionMessage;
import cl.duoc.inscripcion.entities.Curso;
import cl.duoc.inscripcion.entities.Inscripcion;
import cl.duoc.inscripcion.repositories.CursoRepository;
import cl.duoc.inscripcion.repositories.InscripcionRepository;

@Service
public class InscripcionService {

    private static final Logger log = LoggerFactory.getLogger(InscripcionService.class);

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ResumenProductorService resumenProductorService;

    public Inscripcion inscribir(InscripcionRequest request) {

        if (request.getCursosIds() == null || request.getCursosIds().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un curso");
        }

        List<Curso> cursos = cursoRepository.findAllById(request.getCursosIds());

        if (cursos.size() != request.getCursosIds().size()) {
            throw new IllegalArgumentException("Uno o mas cursos no existen en la base de datos");
        }

        double subtotal = cursos.stream().mapToDouble(Curso::getCosto).sum();
        double iva = subtotal * 0.19;
        double total = Math.round((subtotal + iva) * 100.0) / 100.0;

        Inscripcion ins = new Inscripcion();
        ins.setNombreEstudiante(request.getNombreEstudiante());
        ins.setEmailEstudiante(request.getEmailEstudiante());
        ins.setFechaInscripcion(LocalDateTime.now());
        ins.setCursos(cursos);
        ins.setTotalPagar(total);

        Inscripcion inscripcionGuardada = inscripcionRepository.save(ins);

        // Semana 7: se envia el resumen de la inscripcion (recien creada, Semana 1)
        // hacia la cola RabbitMQ de forma asincrona. El consumidor la procesara y
        // guardara el resumen de compra en la nueva tabla resumen_compra.
        try {
            List<String> nombresCursos = cursos.stream().map(Curso::getNombre).toList();
            ResumenInscripcionMessage resumen = new ResumenInscripcionMessage(
                    inscripcionGuardada.getId(),
                    inscripcionGuardada.getNombreEstudiante(),
                    inscripcionGuardada.getEmailEstudiante(),
                    inscripcionGuardada.getFechaInscripcion(),
                    nombresCursos,
                    subtotal,
                    Math.round(iva * 100.0) / 100.0,
                    inscripcionGuardada.getTotalPagar());
            resumenProductorService.enviarResumen(resumen);
        } catch (Exception e) {
            // La inscripcion ya quedo persistida; un fallo al publicar en la cola
            // no debe interrumpir la respuesta al usuario, solo se registra el error.
            log.error("No se pudo enviar el resumen de la inscripcion id={} a la cola RabbitMQ: {}",
                    inscripcionGuardada.getId(), e.getMessage(), e);
        }

        return inscripcionGuardada;
    }

    public List<Inscripcion> listarTodas() {
        return inscripcionRepository.findAll();
    }

    public Inscripcion buscarPorId(Long id) {
        return inscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe inscripcion con id " + id));
    }
}
