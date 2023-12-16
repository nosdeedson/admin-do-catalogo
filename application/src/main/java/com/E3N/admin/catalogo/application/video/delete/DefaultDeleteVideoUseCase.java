package com.E3N.admin.catalogo.application.video.delete;

import e3n.com.admin.catalogo.domain.video.MediaResourceGateway;
import e3n.com.admin.catalogo.domain.video.VideoGateway;
import e3n.com.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

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
