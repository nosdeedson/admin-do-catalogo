package com.E3N.admin.catalogo.application.video.create;

import com.E3N.admin.catalogo.domain.video.Video;

public record CreateVideoOutput(String id) {
    public static CreateVideoOutput from(final Video video){
        return new CreateVideoOutput(video.getId().getValue());
    }
}
