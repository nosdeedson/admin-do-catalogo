package e3n.com.admin.catalogo.application.video.media.upload;

import e3n.com.admin.catalogo.domain.video.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource resource
) {
    public static UploadMediaCommand with(final String videoId, final VideoResource resource){
        return new UploadMediaCommand(videoId, resource);
    }
}
