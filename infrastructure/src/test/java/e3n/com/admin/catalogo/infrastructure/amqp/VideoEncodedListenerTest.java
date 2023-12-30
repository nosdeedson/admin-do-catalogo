package e3n.com.admin.catalogo.infrastructure.amqp;

import com.E3N.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.E3N.admin.catalogo.application.video.media.update.UpdateMeidaStatusCommand;
import com.E3N.admin.catalogo.domain.utils.IdUtils;
import com.E3N.admin.catalogo.domain.video.MediaStatus;
import e3n.com.admin.catalogo.AmqpTest;
import e3n.com.admin.catalogo.infrastructure.configuration.annotations.VideoEncodedQueue;
import e3n.com.admin.catalogo.infrastructure.configuration.json.Json;
import e3n.com.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import e3n.com.admin.catalogo.infrastructure.video.models.videoencoder.VideoEncoderCompleted;
import e3n.com.admin.catalogo.infrastructure.video.models.videoencoder.VideoEncoderError;
import e3n.com.admin.catalogo.infrastructure.video.models.videoencoder.VideoMessage;
import e3n.com.admin.catalogo.infrastructure.video.models.videoencoder.VideoMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

@AmqpTest
public class VideoEncodedListenerTest {

    @Autowired
    private TestRabbitTemplate testRabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private UpdateMediaStatusUseCase updateMediaStatusUseCase;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties queueProperties;

    @Test
    public void testInjections(){
        Assertions.assertNotNull(testRabbitTemplate);
        Assertions.assertNotNull(harness);
        Assertions.assertNotNull(updateMediaStatusUseCase);
        Assertions.assertNotNull(queueProperties);
    }

    @Test
    public void givenErrorResult_whenCallsListener_shouldProcess() throws InterruptedException {
        final var expectedError = new VideoEncoderError(new VideoMessage("123", "path"), "video not found");
        final var expectedMessage = Json.writeValueAsString(expectedError);
        this.testRabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocation = harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocation);
        Assertions.assertNotNull(invocation.getArguments());

        final var message = (String) invocation.getArguments()[0];
        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    public void givenCompletedResult_whenCallsListener_shouldCallUseCase() throws InterruptedException {
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "bucket";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedEncoderVideoFolder = "folder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata = new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

        final var videoEncoderCompleted = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);

        final var expectedMessage = Json.writeValueAsString(videoEncoderCompleted);
        Mockito.doNothing().when(updateMediaStatusUseCase).execute(Mockito.any());
        this.testRabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocation = harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocation.getArguments());

        final var message = (String) invocation.getArguments()[0];

        Assertions.assertEquals(expectedMessage, message);

        final var captor = ArgumentCaptor.forClass(UpdateMeidaStatusCommand.class);
        Mockito.verify(updateMediaStatusUseCase).execute(captor.capture());
        
        final var command = captor.getValue();
        Assertions.assertEquals(expectedStatus, command.status());
        Assertions.assertEquals(expectedId, command.videoId());
        Assertions.assertEquals(expectedResourceId, command.resourceId());
        Assertions.assertEquals(expectedEncoderVideoFolder, command.folder());
        Assertions.assertEquals(expectedFilePath, command.fileName());

    }
}
