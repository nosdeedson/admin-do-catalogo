package e3n.com.admin.catalogo.application.video.create;

import e3n.com.admin.catalogo.domain.video.Video;

public record CreateVideoOutput(String id) {
    public static CreateVideoOutput from(final Video video){
        return new CreateVideoOutput(video.getId().getValue());
    }
}
