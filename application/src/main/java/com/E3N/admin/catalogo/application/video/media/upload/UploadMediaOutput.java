package com.E3N.admin.catalogo.application.video.media.upload;

import com.E3N.admin.catalogo.domain.video.Video;
import com.E3N.admin.catalogo.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType type
) {

    public static UploadMediaOutput with(final Video video, final VideoMediaType type){
        return new UploadMediaOutput(video.getId().getValue(), type);
    }
}
