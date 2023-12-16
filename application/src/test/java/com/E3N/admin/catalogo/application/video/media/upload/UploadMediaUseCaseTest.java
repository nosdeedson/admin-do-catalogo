package com.E3N.admin.catalogo.application.video.media.upload;

import com.E3N.admin.catalogo.application.Fixture;
import com.E3N.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.video.MediaResourceGateway;
import e3n.com.admin.catalogo.domain.video.VideoGateway;
import e3n.com.admin.catalogo.domain.video.VideoMediaType;
import e3n.com.admin.catalogo.domain.video.VideoResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UploadMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUploadMediaUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private MediaResourceGateway resourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, resourceGateway);
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateVideoMediaAndPersistIt() {
        final var video = Fixture.Videos.systemDesign();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.audioVideoMedia(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(video));

        Mockito.when(resourceGateway.storeAudioVideo(Mockito.any(), Mockito.any()))
                .thenReturn(expectedMedia);

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        final var output = this.useCase.execute(command);
        Assertions.assertEquals(expectedType, output.type());
        Assertions.assertEquals(expectedId.getValue(), output.videoId());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(resourceGateway, Mockito.times(1)).storeAudioVideo(Mockito.eq(expectedId),
                Mockito.eq(expectedVideoResource));

        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(cmd ->
                Objects.equals(expectedMedia, cmd.getVideo().get())
                        && cmd.getTrailer().isEmpty()
                        && cmd.getThumbnail().isEmpty()
                        && cmd.getBanner().isEmpty()
                        && cmd.getThumbNailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateTrailerMediaAndPersistIt() {
        final var trailer = Fixture.Videos.systemDesign();
        final var expectedId = trailer.getId();
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedTrailerResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.audioVideoMedia(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(trailer));

        Mockito.when(resourceGateway.storeAudioVideo(expectedId, expectedTrailerResource))
                .thenReturn(expectedMedia);

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedTrailerResource);

        final var output = useCase.execute(command);

        Assertions.assertEquals(expectedType, output.type());
        Assertions.assertEquals(expectedId.getValue(), output.videoId());

        // times is default
        Mockito.verify(videoGateway).findById(Mockito.eq(expectedId));
        Mockito.verify(resourceGateway, Mockito.times(1)).storeAudioVideo(Mockito.eq(expectedId),
                Mockito.eq(expectedTrailerResource));

        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(cmd ->
                Objects.equals(expectedMedia, cmd.getTrailer().get())
                        && cmd.getBanner().isEmpty()
                        && cmd.getThumbNailHalf().isEmpty()
                        && cmd.getVideo().isEmpty()
                        && cmd.getThumbNailHalf().isEmpty()
                        && cmd.getThumbNailHalf().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateBannerMediaAndPersistIt() {
        final var banner = Fixture.Videos.systemDesign();
        final var expectedId = banner.getId();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedBannerResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(banner));

        Mockito.when(resourceGateway.storeImage(expectedId, expectedBannerResource))
                .thenReturn(expectedMedia);

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedBannerResource);

        final var output = useCase.execute(command);

        Assertions.assertEquals(expectedId.getValue(), output.videoId());
        Assertions.assertEquals(expectedType, output.type());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(resourceGateway, Mockito.times(1)).storeImage(Mockito.eq(expectedId),
                Mockito.eq(expectedBannerResource));

        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(cmd ->
                Objects.equals(expectedMedia, cmd.getBanner().get())
                        && cmd.getVideo().isEmpty()
                        && cmd.getThumbNailHalf().isEmpty()
                        && cmd.getThumbnail().isEmpty()
                        && cmd.getTrailer().isEmpty()
        ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailMediaAndPersistIt() {
        final var tumbNail = Fixture.Videos.systemDesign();
        final var expectedId = tumbNail.getId();
        final var expectedType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedThumbNailResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(tumbNail));
        Mockito.when(resourceGateway.storeImage(expectedId, expectedThumbNailResource))
                .thenReturn(expectedMedia);
        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedThumbNailResource);
        useCase.execute(command);
        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(resourceGateway, Mockito.times(1)).storeImage(Mockito.eq(expectedId),
                Mockito.eq(expectedThumbNailResource));
        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(cmd ->
                Objects.equals(expectedMedia, cmd.getThumbnail().get())
                        && cmd.getVideo().isEmpty()
                        && cmd.getTrailer().isEmpty()
                        && cmd.getThumbNailHalf().isEmpty()
                        && cmd.getBanner().isEmpty()
        ));

    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailHalfMediaAndPersistIt() {
        final var tumbNailHalf = Fixture.Videos.systemDesign();
        final var expectedId = tumbNailHalf.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedThumbNailResource = VideoResource.with(expectedType, expectedResource);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(tumbNailHalf));
        Mockito.when(resourceGateway.storeImage(expectedId, expectedThumbNailResource))
                .thenReturn(expectedMedia);
        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedThumbNailResource);
        useCase.execute(command);
        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(resourceGateway, Mockito.times(1)).storeImage(Mockito.eq(expectedId),
                Mockito.eq(expectedThumbNailResource));
        Mockito.verify(videoGateway, Mockito.times(1)).update(Mockito.argThat(cmd ->
                Objects.equals(expectedMedia, cmd.getThumbNailHalf().get())
                        && cmd.getVideo().isEmpty()
                        && cmd.getTrailer().isEmpty()
                        && cmd.getThumbnail().isEmpty()
                        && cmd.getBanner().isEmpty()
        ));

    }

    @Test
    public void givenCmdToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
        final var video = Fixture.Videos.systemDesign();
        final var expectedId = video.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);

        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        final var command = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        final var exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
