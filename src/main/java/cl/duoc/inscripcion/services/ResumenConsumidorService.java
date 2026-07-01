package cl.duoc.inscripcion.services;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.inscripcion.dto.ResumenInscripcionMessage;
import cl.duoc.inscripcion.entities.ResumenCompra;
import cl.duoc.inscripcion.repositories.ResumenCompraRepository;

/**
 * Consumidor de mensajes: escucha la cola de RabbitMQ configurada en
 * {@code app.rabbitmq.resumen.queue} y persiste cada resumen recibido
 * en la nueva tabla resumen_compra.
 *
 * Este componente cumple el rol de "endpoint consumidor" solicitado en
 * la actividad: es el punto de entrada de la aplicacion que procesa
 * los mensajes entrantes desde la cola (en vez de una peticion HTTP,
 * la entrada es un mensaje AMQP).
 */
@Service
public class ResumenConsumidorService {

    private static final Logger log = LoggerFactory.getLogger(ResumenConsumidorService.class);

    @Autowired
    private ResumenCompraRepository resumenCompraRepository;

    @RabbitListener(queues = "${app.rabbitmq.resumen.queue}")
    public void recibirResumen(ResumenInscripcionMessage mensaje) {
        log.info("Mensaje recibido desde la cola RabbitMQ: inscripcionId={}, estudiante={}",
                mensaje.getInscripcionId(), mensaje.getNombreEstudiante());

        // Idempotencia: evita duplicar el resumen si RabbitMQ reintrega el mensaje
        if (resumenCompraRepository.existsByInscripcionId(mensaje.getInscripcionId())) {
            log.warn("El resumen de la inscripcion id={} ya fue procesado anteriormente, se omite.",
                    mensaje.getInscripcionId());
            return;
        }

        ResumenCompra resumen = new ResumenCompra();
        resumen.setInscripcionId(mensaje.getInscripcionId());
        resumen.setNombreEstudiante(mensaje.getNombreEstudiante());
        resumen.setEmailEstudiante(mensaje.getEmailEstudiante());
        resumen.setFechaInscripcion(mensaje.getFechaInscripcion());
        resumen.setCursos(mensaje.getCursos());
        resumen.setSubtotal(mensaje.getSubtotal());
        resumen.setIva(mensaje.getIva());
        resumen.setTotalPagar(mensaje.getTotalPagar());
        resumen.setFechaProcesado(LocalDateTime.now());

        resumenCompraRepository.save(resumen);

        log.info("Resumen de compra guardado en la tabla resumen_compra con id={}", resumen.getId());
    }
}
