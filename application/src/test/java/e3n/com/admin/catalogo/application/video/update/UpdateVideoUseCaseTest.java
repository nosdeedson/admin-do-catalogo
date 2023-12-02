package e3n.com.admin.catalogo.application.video.update;

import e3n.com.admin.catalogo.application.Fixture;
import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.utils.StringUtils;
import e3n.com.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

public class UpdateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway memberGateway;

    @Mock
    private MediaResourceGateway resourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, categoryGateway, memberGateway, genreGateway, resourceGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() {
        final var video = Fixture.Videos.systemDesign();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.aBoolean();
        final var expectedPublished = Fixture.aBoolean();
        final var expectedRating = Fixture.Videos.rating().getName();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId(),
                Fixture.Categories.lives().getId());
        final var expectedGenres = Set.of(Fixture.Genres.business().getId(),
                Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.eva().getId(),
                Fixture.CastMembers.mariana().getId());

        final Resource expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final Resource expectedThumbnail = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbnailHalf = Fixture.Videos.resource(VideoMediaType.THUMBNAIL_HALF);


        final var command = UpdateVideoCommand.with(
                video.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                StringUtils.asString(expectedCategories).collect(Collectors.toSet()),
                StringUtils.asString(expectedGenres).collect(Collectors.toSet()),
                StringUtils.asString(expectedMembers).collect(Collectors.toSet()),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf
        );

        Mockito.when(videoGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(Video.with(video)));

        Mockito.when(categoryGateway.existByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        Mockito.when(memberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        mockAudioVideoMedia();
        mockImageMedia();

        Mockito.when(videoGateway.update(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var result = useCase.execute(command);

        Assertions.assertNotNull(result.id());

        Mockito.verify(videoGateway, Mockito.times(1)).findById(Mockito.eq(video.getId()));

        Mockito.verify(videoGateway).update(Mockito.argThat(cmd ->
                Objects.equals(expectedTitle, cmd.getTitle())
                        && Objects.equals(expectedDescription, cmd.getDescription())
                        && Objects.equals(expectedLaunchedYear, cmd.getLaunchedAt().getValue())
                        && Objects.equals(expectedDuration, cmd.getDuration())
                        && Objects.equals(expectedOpened, cmd.isOpened())
                        && Objects.equals(expectedPublished, cmd.isPublished())
                        && Objects.equals(expectedRating, cmd.getRating().getName())
                        && Objects.equals(expectedCategories.size(), cmd.getCategories().size())
                        && Objects.equals(expectedGenres.size(), cmd.getGenres().size())
                        && Objects.equals(expectedMembers.size(), cmd.getCastMembers().size())
                        && Objects.equals(expectedVideo.name(), cmd.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), cmd.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), cmd.getBanner().get().name())
                        && Objects.equals(expectedThumbnail.name(), cmd.getThumbnail().get().name())
                        && Objects.equals(expectedThumbnailHalf.name(), cmd.getThumbNailHalf().get().name())
        ));
    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsUpdateVideo_shouldReturnVideoId() {

    }

    @Test
    public void givenAValidCommandWithoutGenres_whenCallsUpdateVideo_shouldReturnVideoId() {

    }

    @Test
    public void givenAValidCommandWithoutCastMembers_whenCallsUpdateVideo_shouldReturnVideoId() {

    }

    @Test
    public void givenAValidCommandWithoutResources_whenCallsUpdateVideo_shouldReturnVideoId() {

    }

    @Test
    public void givenANullTitle_whenCallsUpdateVideo_shouldReturnDomainException() {

    }

    @Test
    public void givenAEmptyTitle_whenCallsUpdateVideo_shouldReturnDomainException() {

    }

    @Test
    public void givenANullRating_whenCallsUpdateVideo_shouldReturnDomainException() {}

    @Test
    public void givenAnInvalidRating_whenCallsUpdateVideo_shouldReturnDomainException() {}

    @Test
    public void givenANullLaunchedAt_whenCallsUpdateVideo_shouldReturnDomainException() {}
    @Test
    public void givenAValidCommand_whenCallsUpdateVideoAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {}

    @Test
    public void givenAValidCommand_whenCallsUpdateVideoAndSomeGenresDoesNotExists_shouldReturnDomainException() {}

    @Test
    public void givenAValidCommand_whenCallsUpdateVideoAndSomeCastMembersDoesNotExists_shouldReturnDomainException() {}

    @Test
    public void givenAValidCommand_whenCallsCreateVideoThrowsException_shouldCallClearResources() {}

    
    private void mockAudioVideoMedia() {
        Mockito.when(resourceGateway.storeAudioVideo(Mockito.any(), Mockito.any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return AudioVideoMedia.with(resource.checksum(), resource.name(), "/img");
        });
    }

    private void mockImageMedia() {
        Mockito.when(resourceGateway.storeImage( Mockito.any(), Mockito.any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return ImageMedia.with(resource.checksum(), resource.name(), "/img");
        });
    }
}
