package cl.duoc.inscripcion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.inscripcion.entities.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
