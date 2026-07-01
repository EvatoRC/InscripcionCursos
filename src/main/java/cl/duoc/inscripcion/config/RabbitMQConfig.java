package cl.duoc.inscripcion.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion del servicio de colas RabbitMQ.
 *
 * Define el exchange, la cola y el binding utilizados para publicar y
 * consumir el resumen de una inscripcion (requisito de la Semana 1).
 *
 * Se agrega ademas una cola de mensajes muertos (Dead Letter Queue) para
 * que los mensajes que fallen en su procesamiento (luego de agotar los
 * reintentos configurados en application.properties) no se pierdan y
 * puedan ser inspeccionados/reprocesados.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.resumen.queue}")
    private String resumenQueueName;

    @Value("${app.rabbitmq.resumen.exchange}")
    private String resumenExchangeName;

    @Value("${app.rabbitmq.resumen.routing-key}")
    private String resumenRoutingKey;

    @Value("${app.rabbitmq.resumen.dlq}")
    private String resumenDlqName;

    /** Exchange de tipo direct usado por el productor de resumenes. */
    @Bean
    public DirectExchange resumenExchange() {
        return new DirectExchange(resumenExchangeName, true, false);
    }

    /**
     * Cola durable donde se publica el resumen de la inscripcion.
     * Se enlaza a la DLQ para reenviar automaticamente los mensajes
     * que agoten sus reintentos de procesamiento.
     */
    @Bean
    public Queue resumenQueue() {
        return QueueBuilder.durable(resumenQueueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", resumenDlqName)
                .build();
    }

    /** Cola de mensajes muertos (Dead Letter Queue). */
    @Bean
    public Queue resumenDeadLetterQueue() {
        return QueueBuilder.durable(resumenDlqName).build();
    }

    @Bean
    public Binding resumenBinding() {
        return BindingBuilder.bind(resumenQueue())
                .to(resumenExchange())
                .with(resumenRoutingKey);
    }

    /** Convierte los mensajes hacia/desde JSON automaticamente. */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /** RabbitTemplate (productor) configurado para serializar en JSON. */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        template.setExchange(resumenExchangeName);
        template.setRoutingKey(resumenRoutingKey);
        return template;
    }
}
