package com.E3N.admin.catalogo.application.video.retrieve.get;

import com.E3N.admin.catalogo.application.Fixture;
import com.E3N.admin.catalogo.application.UseCaseTest;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import com.E3N.admin.catalogo.domain.utils.StringUtils;
import com.E3N.admin.catalogo.domain.video.Video;
import com.E3N.admin.catalogo.domain.video.VideoGateway;
import com.E3N.admin.catalogo.domain.video.VideoID;
import com.E3N.admin.catalogo.domain.video.VideoMediaType;
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
    private DefaultGetVideoByIdUseCase useCase;

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
        Assertions.assertTrue(
                StringUtils.asString(expectedCategories).toList().size() ==
                        result.categories().stream().map(CategoryID::from).map(CategoryID::getValue).toList().size()
                && StringUtils.asString(expectedCategories).toList().containsAll(result.categories().stream().map(CategoryID::from).map(CategoryID::getValue).toList())
        );
        Assertions.assertTrue(StringUtils.asString(expectedGenres).toList().size() ==
                result.genres().stream().map(GenreId::from).map(GenreId::getValue).toList().size()
                && StringUtils.asString(expectedGenres).toList().containsAll(
                result.genres().stream().map(GenreId::from).map(GenreId::getValue).toList())
        );
        Assertions.assertTrue(StringUtils.asString(expectedMembers).toList().size() ==
                result.castMembers().stream().map(CastMemberID::from).map(CastMemberID::getValue).toList().size()
                && StringUtils.asString(expectedMembers).toList().containsAll(
                result.castMembers().stream().map(CastMemberID::from).map(CastMemberID::getValue).toList())
        );

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
