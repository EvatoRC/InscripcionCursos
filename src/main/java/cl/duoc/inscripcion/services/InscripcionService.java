package cl.duoc.inscripcion.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.inscripcion.dto.InscripcionRequest;
import cl.duoc.inscripcion.entities.Curso;
import cl.duoc.inscripcion.entities.Inscripcion;
import cl.duoc.inscripcion.repositories.CursoRepository;
import cl.duoc.inscripcion.repositories.InscripcionRepository;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private CursoRepository cursoRepository;

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

        return inscripcionRepository.save(ins);
    }

    public List<Inscripcion> listarTodas() {
        return inscripcionRepository.findAll();
    }

    public Inscripcion buscarPorId(Long id) {
        return inscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe inscripcion con id " + id));
    }
}
