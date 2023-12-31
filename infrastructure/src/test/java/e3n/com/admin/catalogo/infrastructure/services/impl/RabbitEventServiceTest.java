package e3n.com.admin.catalogo.infrastructure.services.impl;

import com.E3N.admin.catalogo.domain.video.VideoMediaCreated;
import e3n.com.admin.catalogo.AmqpTest;
import e3n.com.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import e3n.com.admin.catalogo.infrastructure.configuration.json.Json;
import e3n.com.admin.catalogo.infrastructure.services.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@AmqpTest
public class RabbitEventServiceTest {

    private static final String LISTENER = "video.created";

    @Autowired
    @VideoCreatedQueue
    private EventService eventService;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void testInjections(){
        Assertions.assertNotNull(eventService);
        Assertions.assertNotNull(harness);
    }

    @Test
    public void shouldSendMesage() throws InterruptedException{
//        final var message = new VideoMediaCreated("resource", "filepath");
//        final var expectedMessage = Json.writeValueAsString(message);
//
//        this.eventService.send(message);
//        final var invocationData = harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);
//
//        Assertions.assertNotNull(invocationData);
//        Assertions.assertNotNull(invocationData.getArguments());
//        final var currentMessage = (String) invocationData.getArguments()[0];
//        Assertions.assertEquals(expectedMessage, currentMessage);
    }

    @Component
    static class VideoCreatedNewsListener{
        @RabbitListener(id = LISTENER, queues = "${amqp.queues.video-created.routing-key}")
        void onVideoCreated(@Payload String message){
            System.out.println("message = " + message);
        }
    }
}
