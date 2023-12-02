package e3n.com.admin.catalogo.application.video.media.update;

import e3n.com.admin.catalogo.domain.video.MediaStatus;

public record UpdateMeidaStatusCommand(
        MediaStatus status,
        String videoId,
        String resourceId,
        String folder,
        String fileName
) {
    public static UpdateMeidaStatusCommand with(
            final MediaStatus status,
            final String videoId,
            final String resourceId,
            final String folder,
            final String fileName
    ) {
        return new UpdateMeidaStatusCommand(status, videoId, resourceId,
                folder, fileName);
    }
}
