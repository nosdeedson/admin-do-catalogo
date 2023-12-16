package com.E3N.admin.catalogo.application.video.media.update;

import com.E3N.admin.catalogo.application.Fixture;
import com.E3N.admin.catalogo.application.UseCaseTest;
import com.E3N.admin.catalogo.domain.video.MediaStatus;
import com.E3N.admin.catalogo.domain.video.Video;
import com.E3N.admin.catalogo.domain.video.VideoGateway;
import com.E3N.admin.catalogo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

public class UpdateMediaStatusUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateMediaUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenCommandForVideo_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFileName = "name.mp4";
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideoMedia(expectedType);


        final var video = Fixture.Videos.systemDesign();
        video.updateVideoMedia(expectedMedia);
        final var expectedId = video.getId();

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(video));

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = UpdateMeidaStatusCommand.with(expectedStatus, expectedId.getValue(),
                expectedMedia.id(), expectedFolder, expectedFileName);

        this.useCase.execute(command);

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        Mockito.verify(videoGateway, Mockito.times(1)).update(captor.capture());

        final var result = captor.getValue();

        Assertions.assertTrue(result.getTrailer().isEmpty());

        final var actualVideo = result.getVideo().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideo.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideo.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideo.checksum());
        Assertions.assertEquals(expectedStatus, actualVideo.status());
        Assertions.assertEquals(expectedFolder.concat("/"+expectedFileName), actualVideo.encodedLocation());

    }

    @Test
    public void givenCommandForVideo_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFilename = null;
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideoMedia(expectedType);

        final var video = Fixture.Videos.systemDesign();
        video.updateVideoMedia(expectedMedia);

        final var expectedId = video.getId();

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(video));

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = UpdateMeidaStatusCommand.with(expectedStatus, expectedId.getValue(), expectedMedia.id(),
                expectedFolder, expectedFilename);

        this.useCase.execute(command);
        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        Mockito.verify(videoGateway, Mockito.times(1)).update(captor.capture());
        final var actualVideo = captor.getValue();

        Assertions.assertNotNull(actualVideo);
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        final var actualVideoMedia = actualVideo.getVideo().get();

        Assertions.assertEquals(expectedId, actualVideo.getId());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertEquals(expectedMedia.encodedLocation(), actualVideoMedia.encodedLocation());
        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.name(), actualVideoMedia.name());

    }

    @Test
    public void givenCommandForTrailer_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFileName = "name.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideoMedia(expectedType);

        final var trailer =Fixture.Videos.systemDesign()
                .updateTrailerMedia(expectedMedia);

        final var expectedId = trailer.getId();
        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(trailer));

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = UpdateMeidaStatusCommand.with(expectedStatus, expectedId.getValue(), expectedMedia.id(),
                expectedFolder, expectedFileName);

        this.useCase.execute(command);

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        final var captor = ArgumentCaptor.forClass(Video.class);

        Mockito.verify(videoGateway, Mockito.times(1)).update(captor.capture());

        final var actualTrailer = captor.getValue();

        Assertions.assertNotNull(actualTrailer);
        Assertions.assertTrue(actualTrailer.getVideo().isEmpty());

        final var actualVideoMedia = actualTrailer.getTrailer().get();

        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedFolder.concat("/" + expectedFileName), actualVideoMedia.encodedLocation());
    }

    @Test
    public void givenCommandForTrailer_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFilename = null;
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideoMedia(expectedType);

        final var trailer = Fixture.Videos.systemDesign();
        trailer.updateVideoMedia(expectedMedia);

        final var expectedId = trailer.getId();

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(trailer));

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = UpdateMeidaStatusCommand.with(expectedStatus, expectedId.getValue(), expectedMedia.id(),
                expectedFolder, expectedFilename);

        this.useCase.execute(command);
        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        final var captor = ArgumentCaptor.forClass(Video.class);

        Mockito.verify(videoGateway, Mockito.times(1)).update(captor.capture());
        final var actualVideo = captor.getValue();

        Assertions.assertNotNull(actualVideo);
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        final var actualVideoMedia = actualVideo.getVideo().get();

        Assertions.assertEquals(expectedId, actualVideo.getId());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertEquals(expectedMedia.encodedLocation(), actualVideoMedia.encodedLocation());
        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.name(), actualVideoMedia.name());
    }

    @Test
    public void givenCommandForTrailer_whenIsInvalid_shouldDoNothing() {
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFilename = "name.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideoMedia(expectedType);

        final var trailer = Fixture.Videos.systemDesign();
        trailer.updateVideoMedia(expectedMedia);

        final var expectedId = trailer.getId();
        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(trailer));

        final var command = UpdateMeidaStatusCommand.with(expectedStatus, expectedId.getValue(), "invalidId",
                expectedFolder, expectedFilename);

        this.useCase.execute(command);

        Mockito.verify(videoGateway, Mockito.times(0)).update(Mockito.any());
    }
}
