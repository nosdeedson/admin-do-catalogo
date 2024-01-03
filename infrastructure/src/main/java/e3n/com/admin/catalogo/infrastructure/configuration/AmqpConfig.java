package e3n.com.admin.catalogo.infrastructure.configuration;


import e3n.com.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import e3n.com.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue;
import e3n.com.admin.catalogo.infrastructure.configuration.annotations.VideoEvents;
import e3n.com.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.video-created")
    @VideoCreatedQueue
    QueueProperties videoCreatedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queues.video-encoded")
    @VideoEncodedQueue
    QueueProperties videoEncodedProperties() {
        return new QueueProperties();
    }

    @Configuration
    static class Admin {

        @Bean
        @VideoEvents
        Exchange videoEventsExchange(@VideoCreatedQueue QueueProperties props) {
            return new DirectExchange(props.getExchange());
        }

        @Bean
        @VideoCreatedQueue
        Queue videoCreatedQueue(@VideoCreatedQueue QueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        @VideoCreatedQueue
        Binding videoCreateQueue(
                @VideoCreatedQueue QueueProperties props,
                @VideoEvents DirectExchange exchange,
                @VideoCreatedQueue Queue queue) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }

        @Bean
        @VideoEncodedQueue
        Queue videoEncodedQueue(@VideoEncodedQueue QueueProperties properties){
            return new Queue(properties.getQueue());
        }

        @Bean
        @VideoEncodedQueue
        Binding videoEncodedeQueue(
                @VideoCreatedQueue QueueProperties props,
                @VideoEvents DirectExchange exchange,
                @VideoCreatedQueue Queue queue) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }
    }

}
