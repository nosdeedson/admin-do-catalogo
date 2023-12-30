package e3n.com.admin.catalogo.infrastructure.video.models.videoencoder;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ERROR")

public record VideoEncoderError(
        @JsonProperty("message") VideoMessage message,
        @JsonProperty("error") String error
) implements VideoEncoderResult{
    private static final String ERROR = "ERROR";
    @Override
    public String getStatus() {
        return ERROR;
    }
}
