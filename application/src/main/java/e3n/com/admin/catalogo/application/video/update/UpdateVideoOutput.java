package e3n.com.admin.catalogo.application.video.update;

import e3n.com.admin.catalogo.domain.video.Video;

public record UpdateVideoOutput(
        String id
) {
    public static UpdateVideoOutput from(final Video video){
        return new UpdateVideoOutput(video.getId().getValue());
    }
}
