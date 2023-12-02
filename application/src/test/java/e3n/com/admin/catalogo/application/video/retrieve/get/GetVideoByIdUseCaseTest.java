package e3n.com.admin.catalogo.application.video.retrieve.get;

import e3n.com.admin.catalogo.application.Fixture;
import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.Identifier;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.genre.GenreId;
import e3n.com.admin.catalogo.domain.utils.StringUtils;
import e3n.com.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GetVideoByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetVidoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetVideo_shouldReturnIt() {
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.aBoolean();
        final var expectedPublished = Fixture.aBoolean();
        final var expectedRating = Fixture.Videos.rating();

        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId(), Fixture.Categories.lives().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId(), Fixture.Genres.business().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.eva().getId(), Fixture.CastMembers.mariana().getId());

        final var expectedVideo = Fixture.Videos.audioVideoMedia(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideoMedia(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumbnail = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbnailHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var video = Video.newVideo(expectedTitle, expectedDescription, Year.of(expectedLaunchedAt),
                expectedDuration, expectedOpened, expectedPublished, expectedRating, expectedCategories,
                expectedGenres, expectedMembers);
        video.updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumbnail)
                .updateThumbnailHalfMedia(expectedThumbnailHalf);

        final var expectedVideoId = video.getId();

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(video));


        final var result = this.useCase.execute(expectedVideoId.getValue());

        Assertions.assertEquals(expectedTitle, result.title());
        Assertions.assertEquals(expectedDescription, result.description());
        Assertions.assertEquals(expectedDuration, result.duration());
        Assertions.assertEquals(expectedLaunchedAt, result.launchedAt());
        Assertions.assertEquals(expectedOpened, result.opened());
        Assertions.assertEquals(expectedPublished, result.published());
        Assertions.assertEquals(expectedRating, result.rating());

        Assertions.assertEquals(StringUtils.asString(expectedCategories).toList(),
                result.categories().stream().map(CastMemberID::from).map(CastMemberID::getValue).toList() );
        Assertions.assertEquals(StringUtils.asString(expectedGenres).toList(),
                result.genres().stream().map(GenreId::from).map(GenreId::getValue).toList() );
        Assertions.assertEquals(StringUtils.asString(expectedMembers).toList(),
                result.castMembers().stream().map(CastMemberID::from).map(CastMemberID::getValue).toList() );

        Assertions.assertEquals(expectedVideo, result.video());
        Assertions.assertEquals(expectedBanner, result.banner());
        Assertions.assertEquals(expectedTrailer, result.trailer());
        Assertions.assertEquals(expectedThumbnail, result.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, result.thumbnailHalf());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expectedVideoId));
    }

    @Test
    public void givenInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
        final var expedtedVideoId = VideoID.from("123");
        final var expectedErrorMessage = "Video with ID 123 was not found";

        final var expection = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(expedtedVideoId.getValue()));
        Assertions.assertEquals(expectedErrorMessage, expection.getMessage());
        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(expedtedVideoId));
    }
}
