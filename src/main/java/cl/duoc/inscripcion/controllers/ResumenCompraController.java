package cl.duoc.inscripcion.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.inscripcion.entities.ResumenCompra;
import cl.duoc.inscripcion.repositories.ResumenCompraRepository;

/**
 * Expone los resumenes de compra que el consumidor de RabbitMQ
 * ({@code ResumenConsumidorService}) fue guardando en la nueva tabla
 * resumen_compra a medida que llegaron mensajes desde la cola.
 */
@RestController
@RequestMapping("/resumenes")
public class ResumenCompraController {

    @Autowired
    private ResumenCompraRepository resumenCompraRepository;

    @GetMapping
    public ResponseEntity<List<ResumenCompra>> listarResumenes() {
        return ResponseEntity.ok(resumenCompraRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerResumen(@PathVariable Long id) {
        return resumenCompraRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body("Error: no existe un resumen de compra con id " + id));
    }
}
