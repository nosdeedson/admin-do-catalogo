package com.E3N.admin.catalogo.domain.video;

import com.E3N.admin.catalogo.domain.UnitTest;
import com.E3N.admin.catalogo.domain.utils.IdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AudioVideoMediaTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallsNewAudioVideo_ShouldReturnInstance() {
        final var expectedId = IdUtils.uuid();
        final var expectedChecksum = "asd";
        final var expectedName = "video.mp4";
        final var expectedRawLocation = "/video/as";
        final var expectedEncodedLocation = "/video/as-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        final var video = AudioVideoMedia.with(expectedId, expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus);
        Assertions.assertNotNull(video);
        Assertions.assertEquals(expectedId, video.id());
        Assertions.assertEquals(expectedChecksum, video.checksum());
        Assertions.assertEquals(expectedName, video.name());
        Assertions.assertEquals(expectedRawLocation, video.rawLocation());
        Assertions.assertEquals(expectedEncodedLocation, video.encodedLocation());
        Assertions.assertEquals(expectedStatus, video.status());
    }

    @Test
    public void givenTwoVideosWithSameChecksumAndLocation_whenCallsEquals_ShouldReturnTrue() {
        final var expectedChecksum = "asd";
        final var expectedRawLocation = "/video/as";
        final var video1 = AudioVideoMedia.with(expectedChecksum, "random", expectedRawLocation);
        final var video2 = AudioVideoMedia.with(expectedChecksum, "simple", expectedRawLocation);

        Assertions.assertEquals(video1, video2);
        Assertions.assertNotSame(video1, video2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_ShouldReturnError() {
        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with(null, "123", "teste"));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("id", "123", null, "test", "test", MediaStatus.PENDING));


        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("id", "123", "test", null, "test", MediaStatus.PENDING));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("id", "123", "test", "test", null, MediaStatus.PENDING));

        Assertions.assertThrows(NullPointerException.class,
                () -> AudioVideoMedia.with("id", "123", "test", "test", "teste", null));

    }
}
