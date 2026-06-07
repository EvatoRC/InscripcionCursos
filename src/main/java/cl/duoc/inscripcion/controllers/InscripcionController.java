package cl.duoc.inscripcion.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.inscripcion.dto.BoletaResponse;
import cl.duoc.inscripcion.dto.InscripcionRequest;
import cl.duoc.inscripcion.entities.Inscripcion;
import cl.duoc.inscripcion.services.InscripcionService;

@RestController
@RequestMapping("/inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @GetMapping
    public ResponseEntity<List<BoletaResponse>> listarInscripciones() {
        System.out.println("Se recibio llamada a endpoint GET /inscripciones");
        List<BoletaResponse> boletas = inscripcionService.listarTodas().stream()
                .map(BoletaResponse::desdeInscripcion)
                .toList();
        return ResponseEntity.ok(boletas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInscripcion(@PathVariable Long id) {
        System.out.println("Se recibio llamada a endpoint GET /inscripciones/" + id);
        try {
            Inscripcion ins = inscripcionService.buscarPorId(id);
            return ResponseEntity.ok(BoletaResponse.desdeInscripcion(ins));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> inscribir(@RequestBody InscripcionRequest request) {
        System.out.println("Se recibio llamada a endpoint POST /inscripciones para: " + request.getNombreEstudiante());
        try {
            Inscripcion ins = inscripcionService.inscribir(request);
            return ResponseEntity.ok(BoletaResponse.desdeInscripcion(ins));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}