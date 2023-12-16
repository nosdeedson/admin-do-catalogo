package com.E3N.admin.catalogo.application.video.media.update;

import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.video.*;

import java.util.Objects;

public class DefaultUpdateMediaUseCase extends UpdateMediaStatusUseCase {

    private VideoGateway videoGateway;

    public DefaultUpdateMediaUseCase(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(UpdateMeidaStatusCommand command) {
        final var videoId = VideoID.from(command.videoId());
        final var resourceId = command.resourceId();
        final var folder = command.folder();
        final var fileName = command.fileName();

        final var video =
                videoGateway.findById(videoId).orElseThrow(() -> NotFoundException.with(Video.class, videoId));

        final var encodedPath = "%s/%s".formatted(folder, fileName);

        if (matches(resourceId, video.getVideo().orElse(null))) {
            updateVideo(VideoMediaType.VIDEO, command.status(), video,
                    encodedPath);
        }else if (matches(command.resourceId(), video.getTrailer().orElse(null))) {
            updateVideo(VideoMediaType.TRAILER, command.status(), video,
                    encodedPath);
        }
    }

    private void updateVideo(final VideoMediaType type,
                             final MediaStatus status, final Video video,
                             final String encodedPath) {
        switch (status) {
            case PENDING -> {
            }
            case PROCESSING -> video.processing(type);
            case COMPLETED -> video.completed(type, encodedPath);
        }
        this.videoGateway.update(video);
    }

    private boolean matches(final String id, final AudioVideoMedia media) {
        if (media == null) {
            return false;
        }
        return media.id().equals(id);
    }
}
