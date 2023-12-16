package com.E3N.admin.catalogo.application.video.retrieve.get;

import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.video.Video;
import com.E3N.admin.catalogo.domain.video.VideoGateway;
import com.E3N.admin.catalogo.domain.video.VideoID;

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
