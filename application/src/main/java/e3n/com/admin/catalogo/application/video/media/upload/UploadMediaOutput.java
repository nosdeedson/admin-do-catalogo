package e3n.com.admin.catalogo.application.video.media.upload;

import e3n.com.admin.catalogo.domain.video.Video;
import e3n.com.admin.catalogo.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType type
) {

    public static UploadMediaOutput with(final Video video, final VideoMediaType type){
        return new UploadMediaOutput(video.getId().getValue(), type);
    }
}
