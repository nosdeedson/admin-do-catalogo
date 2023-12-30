package e3n.com.admin.catalogo.infrastructure.amqp;


import com.E3N.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.E3N.admin.catalogo.application.video.media.update.UpdateMeidaStatusCommand;
import com.E3N.admin.catalogo.domain.video.MediaStatus;
import e3n.com.admin.catalogo.infrastructure.configuration.json.Json;
import e3n.com.admin.catalogo.infrastructure.video.models.videoencoder.VideoEncoderCompleted;
import e3n.com.admin.catalogo.infrastructure.video.models.videoencoder.VideoEncoderError;
import e3n.com.admin.catalogo.infrastructure.video.models.videoencoder.VideoEncoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoEncoderListener {

    private static final Logger logger = LoggerFactory.getLogger(VideoEncoderListener.class);

    static final String LISTENER_ID = "videoEncodedListener";

    private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

    public VideoEncoderListener(UpdateMediaStatusUseCase updateMediaStatusUseCase) {
        this.updateMediaStatusUseCase = Objects.requireNonNull(updateMediaStatusUseCase);
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
    public void onVideoEncondedMessage(@Payload final String message){
        final var result = Json.readValue(message, VideoEncoderResult.class);
        if (result instanceof VideoEncoderCompleted dto){
            logger.error("[message:video.listener.income] [status::completed] [payload:{}]", message);
            final var command = new UpdateMeidaStatusCommand(MediaStatus.COMPLETED, dto.id(), dto.video().resourceId(), dto.video().encodedVideoFolder(), dto.video().filePath());
            this.updateMediaStatusUseCase.execute(command);
        }else if (result instanceof VideoEncoderError){
            logger.error("[message:video.listener.income] [status.error] [payload:{}]", message);
        }else {
            logger.error("[message:video.listener.income] [status.unknow] [payload:{}]", message);
        }
    }
}
