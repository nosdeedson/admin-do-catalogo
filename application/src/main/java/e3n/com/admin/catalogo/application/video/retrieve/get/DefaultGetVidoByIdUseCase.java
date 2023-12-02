package e3n.com.admin.catalogo.application.video.retrieve.get;

import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.video.Video;
import e3n.com.admin.catalogo.domain.video.VideoGateway;
import e3n.com.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVidoByIdUseCase extends GetVideoByIdUseCase{

    private final VideoGateway videoGateway;

    public DefaultGetVidoByIdUseCase(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String id) {
        return this.videoGateway.findById(VideoID.from(id))
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, VideoID.from(id)));
    }
}
