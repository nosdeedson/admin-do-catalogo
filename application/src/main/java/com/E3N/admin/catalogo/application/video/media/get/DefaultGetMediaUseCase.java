package com.E3N.admin.catalogo.application.video.media.get;

import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.video.MediaResourceGateway;
import e3n.com.admin.catalogo.domain.video.VideoID;
import e3n.com.admin.catalogo.domain.video.VideoMediaType;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase{

    public MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(GetMediaCommand getMediaCommand) {
        final var videoId = VideoID.from(getMediaCommand.videoId());
        final var metiaType = VideoMediaType.of(getMediaCommand.mediaType())
                .orElseThrow(() -> NotFoundException.with(new Error("type of video doesn't exist")));

        final var media = mediaResourceGateway.getResource(videoId, metiaType)
                .orElseThrow(() -> NotFoundException.with(new Error("Media not found")));

        return MediaOutput.with(media);
    }
}
