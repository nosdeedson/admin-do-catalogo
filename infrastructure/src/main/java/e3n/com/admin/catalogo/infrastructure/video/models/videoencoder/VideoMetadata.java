package e3n.com.admin.catalogo.infrastructure.video.models.videoencoder;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoMetadata(
        @JsonProperty("encoded_video_folder") String encodedVideoFolder,
        @JsonProperty("resource_id") String resourceId,
        @JsonProperty("file_path") String filePath
) {
}
