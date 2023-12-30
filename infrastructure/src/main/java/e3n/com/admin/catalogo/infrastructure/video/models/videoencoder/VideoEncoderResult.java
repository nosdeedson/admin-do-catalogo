package e3n.com.admin.catalogo.infrastructure.video.models.videoencoder;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "status")
@VideoResponseTypes
public sealed interface VideoEncoderResult permits VideoEncoderCompleted, VideoEncoderError {
    String getStatus();
}
