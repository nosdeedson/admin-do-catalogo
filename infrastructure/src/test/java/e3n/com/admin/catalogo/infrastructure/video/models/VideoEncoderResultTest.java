package e3n.com.admin.catalogo.infrastructure.video.models;

import com.E3N.admin.catalogo.domain.utils.IdUtils;
import e3n.com.admin.catalogo.JackSonTest;
import e3n.com.admin.catalogo.infrastructure.video.models.videoencoder.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JackSonTest
public class VideoEncoderResultTest {

    @Autowired
    private JacksonTester<VideoEncoderResult> json;

    @Test
    public void testUnmarshallSuccessResult() throws IOException {
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "bucket";
        final var expectedStatus = "COMPLETED";
        final var expectedEncoderVideoFolder = "any";
        final var expectedResouceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata = new VideoMetadata(expectedEncoderVideoFolder, expectedResouceId, expectedFilePath);

        final var json = """
                {
                    "status": "%s",
                    "id": "%s",
                    "output_bucket_path": "%s",
                    "video": {
                        "encoded_video_folder": "%s",
                        "resource_id": "%s",
                        "file_path": "%s"
                    }
                }
                """.formatted(expectedStatus, expectedId, expectedOutputBucket, expectedEncoderVideoFolder, expectedResouceId, expectedFilePath);

        final var result = this.json.parse(json);

        Assertions.assertThat(result)
                .isInstanceOf(VideoEncoderCompleted.class)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("outputBucket", expectedOutputBucket)
                .hasFieldOrPropertyWithValue("video", expectedMetadata);
    }

    @Test
    public void testMarshallSuccessResult() throws IOException {
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "bucket";
        final var expectedStatus = "COMPLETED";
        final var expectedEncoderVideoFolder = "any";
        final var expectedResouceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata = new VideoMetadata(expectedEncoderVideoFolder, expectedResouceId, expectedFilePath);

        final var videoEncoderCompleted = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);

        final var result = this.json.write(videoEncoderCompleted);

        Assertions.assertThat(result)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.output_bucket_path", expectedOutputBucket)
                .hasJsonPathValue("$.status", expectedStatus)
                .hasJsonPathValue("$.video.encoded_video_folder", expectedEncoderVideoFolder)
                .hasJsonPathValue("$.video.resource_id", expectedResouceId)
                .hasJsonPathValue("$.video.file_path", expectedFilePath);
    }

    @Test
    public void testUnmarshallErrorResult() throws IOException {
        final var expectedMessage = "Resource not found";
        final var expectedStatus = "ERROR";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedVideoMessage = new VideoMessage(expectedResourceId,expectedFilePath);

        final var json = """
                    {
                        "status": "%s",
                        "error": "%s",
                        "message": {
                                "resource_id": "%s",
                                "file_path": "%s"
                            }
                    }
                """.formatted(expectedStatus, expectedMessage, expectedResourceId, expectedFilePath);


        final var result = this.json.parse(json);
        Assertions.assertThat(result)
                .isInstanceOf(VideoEncoderError.class)
                .hasFieldOrPropertyWithValue("error", expectedMessage)
                .hasFieldOrPropertyWithValue("message", expectedVideoMessage);
    }

    @Test
    public void testMarshallErrorResult() throws IOException {
        final var expectedMessage = "Resource not found";
        final var expectedStatus = "ERROR";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedVideoMessage = new VideoMessage(expectedResourceId,expectedFilePath);

        final var videoEncoderError = new VideoEncoderError(expectedVideoMessage, expectedMessage);

        final var result = this.json.write(videoEncoderError);

        Assertions.assertThat(result)
                .hasJsonPathValue("$.status", expectedStatus)
                .hasJsonPathValue("$.error", expectedMessage)
                .hasJsonPathValue("$.message.resource_id", expectedResourceId)
                .hasJsonPathValue("$.message.file_path", expectedFilePath);
    }
}
