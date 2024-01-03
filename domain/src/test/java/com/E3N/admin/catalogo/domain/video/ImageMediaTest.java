package com.E3N.admin.catalogo.domain.video;

import com.E3N.admin.catalogo.domain.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImageMediaTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallsNewImage_ShouldReturnInstance() {
        final var expectedChecksum = "123";
        final var expectedName = "banner.png";
        final var expectedLocation = "/images/as";
        final var image = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);
        Assertions.assertNotNull(image);
        Assertions.assertEquals(expectedChecksum, image.checksum());
        Assertions.assertEquals(expectedName, image.name());
        Assertions.assertEquals(expectedLocation, image.location());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_ShouldReturnTrue() {
        final var expectedChecksum = "123";
        final var expectedLocation = "/images/as";

        final var image1 = ImageMedia.with(expectedChecksum, "any", expectedLocation);
        final var image2 = ImageMedia.with(expectedChecksum, "any", expectedLocation);
        Assertions.assertEquals(image2, image1);
        Assertions.assertNotSame(image1, image2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_ShouldReturnError() {
        Assertions.assertThrows(NullPointerException.class, () -> ImageMedia.with(null, "teste", "test"));
        Assertions.assertThrows(NullPointerException.class, () -> ImageMedia.with("teste", null, "test"));
        Assertions.assertThrows(NullPointerException.class, () -> ImageMedia.with("teste", "test", null));

    }

}
