package com.E3N.admin.catalogo.application.video.media.upload;

import com.E3N.admin.catalogo.domain.video.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource resource
) {
    public static UploadMediaCommand with(final String videoId, final VideoResource resource){
        return new UploadMediaCommand(videoId, resource);
    }
}
