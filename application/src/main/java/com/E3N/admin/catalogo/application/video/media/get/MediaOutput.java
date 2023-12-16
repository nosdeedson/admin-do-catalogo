package com.E3N.admin.catalogo.application.video.media.get;

import com.E3N.admin.catalogo.domain.video.Resource;

public record MediaOutput(
        byte[] content,
        String contentType,
        String name
) {
    public static MediaOutput with(final Resource resource){
        return new MediaOutput(resource.content(), resource.contentType(), resource.name());
    }
}
