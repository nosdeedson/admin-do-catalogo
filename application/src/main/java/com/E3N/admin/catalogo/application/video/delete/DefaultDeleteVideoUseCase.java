package com.E3N.admin.catalogo.application.video.delete;

import com.E3N.admin.catalogo.domain.video.MediaResourceGateway;
import com.E3N.admin.catalogo.domain.video.VideoGateway;
import com.E3N.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

@SuppressWarnings("all")
public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase{

    private VideoGateway videoGateway;
    private MediaResourceGateway mediaResourceGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public void execute(final String id) {
        final var videoId = VideoID.from(id);
        this.mediaResourceGateway.clearReources(videoId);
        this.videoGateway.deleteById(videoId);
    }
}
