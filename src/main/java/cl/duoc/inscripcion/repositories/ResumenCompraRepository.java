package cl.duoc.inscripcion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.inscripcion.entities.ResumenCompra;

@Repository
public interface ResumenCompraRepository extends JpaRepository<ResumenCompra, Long> {

    boolean existsByInscripcionId(Long inscripcionId);
}
