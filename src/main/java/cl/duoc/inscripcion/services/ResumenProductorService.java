package cl.duoc.inscripcion.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cl.duoc.inscripcion.dto.ResumenInscripcionMessage;

/**
 * Productor de mensajes: envia el resumen de una inscripcion hacia la
 * cola RabbitMQ para que sea procesado de forma asincrona por el
 * consumidor ({@link ResumenConsumidorService}).
 */
@Service
public class ResumenProductorService {

    private static final Logger log = LoggerFactory.getLogger(ResumenProductorService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.resumen.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.resumen.routing-key}")
    private String routingKey;

    public void enviarResumen(ResumenInscripcionMessage resumen) {
        log.info("Publicando resumen de inscripcion id={} en la cola RabbitMQ (exchange={}, routingKey={})",
                resumen.getInscripcionId(), exchange, routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, resumen);
    }
}
