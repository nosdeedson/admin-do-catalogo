package e3n.com.admin.catalogo.infrastructure.video.models.videoencoder;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoMessage(
        @JsonProperty("resource_id") String resourceId,
        @JsonProperty("file_path") String filePath
) {
}
