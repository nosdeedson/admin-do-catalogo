package com.E3N.admin.catalogo.domain.video;

import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import com.E3N.admin.catalogo.domain.utils.InstantUtils;
import com.E3N.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import com.E3N.admin.catalogo.domain.video.AudioVideoMedia;
import com.E3N.admin.catalogo.domain.video.ImageMedia;
import com.E3N.admin.catalogo.domain.video.Rating;
import com.E3N.admin.catalogo.domain.video.Video;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoTest{

    @Test
    public void givenValidParams_whenCallsNewVideo_shouldInstantiate() {

        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "description ";
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPUblished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.from("123"));
        final var expectedGenres = Set.of(GenreId.from("123"));
        final var expectedMembers = Set.of(CastMemberID.from("123"));

        final var video = Video.newVideo(expectedTitle, expectedDescription, expectedLaunchedAt, //
                expectedDuration, expectedOpened, expectedPUblished, expectedRating, expectedCategories, expectedGenres, expectedMembers);

        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPUblished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertEquals(expectedCategories, video.getCategories());
        Assertions.assertEquals(expectedGenres, video.getGenres());
        Assertions.assertEquals(expectedMembers, video.getCastMembers());
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbNailHalf().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertTrue(video.getDomainEvents().isEmpty());

        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "description ";
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPUblished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.from("123"));
        final var expectedGenres = Set.of(GenreId.from("123"));
        final var expectedMembers = Set.of(CastMemberID.from("123"));

        final var video = Video.newVideo("title", "description", Year.of(2020), //
                50, true, true, Rating.AGE_10, expectedCategories, expectedGenres, expectedMembers);

        final var expectedEventCount = 1;
        final var expectedEvent = new VideoMediaCreated(video.getId().getValue(), "file");

        video.registerEvent(expectedEvent);

        final var afterUpdating = video.getUpdatedAt();
        video.update(expectedTitle, expectedDescription, expectedLaunchedAt, expectedDuration, expectedOpened, expectedPUblished,
                expectedRating, expectedCategories, expectedGenres, expectedMembers);

        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPUblished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertEquals(expectedCategories, video.getCategories());
        Assertions.assertEquals(expectedGenres, video.getGenres());
        Assertions.assertEquals(expectedMembers, video.getCastMembers());
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbNailHalf().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(video.getCreatedAt());
        Assertions.assertTrue(video.getUpdatedAt().isAfter(afterUpdating));

        Assertions.assertEquals(expectedEventCount, video.getDomainEvents().size());
        Assertions.assertEquals(expectedEvent, video.getDomainEvents().get(0));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateVideoMedia_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "description ";
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPUblished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.from("123"));
        final var expectedGenres = Set.of(GenreId.from("123"));
        final var expectedMembers = Set.of(CastMemberID.from("123"));
        final var expectedEventCount = 1;

        final var video = Video.newVideo(expectedTitle, expectedDescription, expectedLaunchedAt, //
                expectedDuration, expectedOpened, expectedPUblished, expectedRating, expectedCategories, expectedGenres, expectedMembers);

        final var videoMedia = AudioVideoMedia.with("qwe", "video.mp4", "abc/video");
        video.updateVideoMedia(videoMedia);

        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPUblished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertEquals(expectedCategories, video.getCategories());
        Assertions.assertEquals(expectedGenres, video.getGenres());
        Assertions.assertEquals(expectedMembers, video.getCastMembers());
        Assertions.assertTrue(video.getVideo().isPresent());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbNailHalf().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertNotNull(video.getCreatedAt());

        Assertions.assertEquals(expectedEventCount, video.getDomainEvents().size());
        final var event = (VideoMediaCreated) video.getDomainEvents().get(0);
        Assertions.assertEquals(video.getId().getValue(), event.resourceId());
        Assertions.assertEquals(videoMedia.rawLocation(), event.filePath());
        Assertions.assertNotNull(event.occurredOn());

        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateTrailerMedia_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "description ";
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPUblished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.from("123"));
        final var expectedGenres = Set.of(GenreId.from("123"));
        final var expectedMembers = Set.of(CastMemberID.from("123"));

        final var expectedEventCount = 1;


        final var video = Video.newVideo(expectedTitle, expectedDescription, expectedLaunchedAt, //
                expectedDuration, expectedOpened, expectedPUblished, expectedRating, expectedCategories, expectedGenres, expectedMembers);

        final var trailer = AudioVideoMedia.with("qwe", "trailer.mp4", "abc/video");
        video.updateTrailerMedia(trailer);

        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPUblished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertEquals(expectedCategories, video.getCategories());
        Assertions.assertEquals(expectedGenres, video.getGenres());
        Assertions.assertEquals(expectedMembers, video.getCastMembers());
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbNailHalf().isEmpty());
        Assertions.assertTrue(video.getTrailer().isPresent());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertNotNull(video.getCreatedAt());

        Assertions.assertEquals(expectedEventCount, video.getDomainEvents().size());

        final var event = (VideoMediaCreated) video.getDomainEvents().get(0);
        Assertions.assertEquals(video.getId().getValue(), event.resourceId());
        Assertions.assertEquals(trailer.rawLocation(), event.filePath());
        Assertions.assertNotNull(event.ocurredon());
    }

    @Test
    public void givenValidVideo_whenCallsUpdateBannerMedia_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "description ";
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPUblished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.from("123"));
        final var expectedGenres = Set.of(GenreId.from("123"));
        final var expectedMembers = Set.of(CastMemberID.from("123"));

        final var video = Video.newVideo(expectedTitle, expectedDescription, expectedLaunchedAt, //
                expectedDuration, expectedOpened, expectedPUblished, expectedRating, expectedCategories, expectedGenres, expectedMembers);

        final var banner = ImageMedia.with("qwe", "banner.png", "abc/video");
        video.updateBannerMedia(banner);

        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPUblished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertEquals(expectedCategories, video.getCategories());
        Assertions.assertEquals(expectedGenres, video.getGenres());
        Assertions.assertEquals(expectedMembers, video.getCastMembers());
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbNailHalf().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertTrue(video.getBanner().isPresent());
        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertNotNull(video.getCreatedAt());
    }

    @Test
    public void givenValidVideo_whenCallsUpdateThumbnailMedia_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "description ";
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPUblished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.from("123"));
        final var expectedGenres = Set.of(GenreId.from("123"));
        final var expectedMembers = Set.of(CastMemberID.from("123"));

        final var video = Video.newVideo(expectedTitle, expectedDescription, expectedLaunchedAt, //
                expectedDuration, expectedOpened, expectedPUblished, expectedRating, expectedCategories, expectedGenres, expectedMembers);

        final var thumbNail = ImageMedia.with("qwe", "thumbnail.png", "abc/video");
        video.updateThumbnailMedia(thumbNail);

        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPUblished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertEquals(expectedCategories, video.getCategories());
        Assertions.assertEquals(expectedGenres, video.getGenres());
        Assertions.assertEquals(expectedMembers, video.getCastMembers());
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isPresent());
        Assertions.assertTrue(video.getThumbNailHalf().isEmpty());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertNotNull(video.getCreatedAt());
    }

    @Test
    public void givenValidVideo_whenCallsUpdateThumbnailHalfMedia_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "description ";
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPUblished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.from("123"));
        final var expectedGenres = Set.of(GenreId.from("123"));
        final var expectedMembers = Set.of(CastMemberID.from("123"));

        final var video = Video.newVideo(expectedTitle, expectedDescription, expectedLaunchedAt, //
                expectedDuration, expectedOpened, expectedPUblished, expectedRating, expectedCategories, expectedGenres, expectedMembers);

        final var thumbnailHalf = ImageMedia.with("qwe", "thumbnailHalf.png", "abc/video");
        video.updateThumbnailHalfMedia(thumbnailHalf);

        Assertions.assertNotNull(video);
        Assertions.assertNotNull(video.getId());
        Assertions.assertEquals(expectedTitle, video.getTitle());
        Assertions.assertEquals(expectedDescription, video.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, video.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, video.getDuration());
        Assertions.assertEquals(expectedOpened, video.isOpened());
        Assertions.assertEquals(expectedPUblished, video.isPublished());
        Assertions.assertEquals(expectedRating, video.getRating());
        Assertions.assertEquals(expectedCategories, video.getCategories());
        Assertions.assertEquals(expectedGenres, video.getGenres());
        Assertions.assertEquals(expectedMembers, video.getCastMembers());
        Assertions.assertTrue(video.getVideo().isEmpty());
        Assertions.assertTrue(video.getThumbnail().isEmpty());
        Assertions.assertTrue(video.getThumbNailHalf().isPresent());
        Assertions.assertTrue(video.getTrailer().isEmpty());
        Assertions.assertTrue(video.getBanner().isEmpty());
        Assertions.assertDoesNotThrow(() -> video.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(video.getUpdatedAt());
        Assertions.assertNotNull(video.getCreatedAt());
    }

    @Test
    public void givenValidVideo_whenCallsWith_shouldCreateWithoutEvents() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "descripion";
        final var expectedLaunchedAt = Year.of(2023);
        final var expectedDuration = 120.0;
        final var expectedOpened = true;
        final var expectedPublished = true;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreId.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var video = Video.with(
                VideoID.unique(),
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                InstantUtils.now(),
                InstantUtils.now(),
                null,
                null,
                null,
                null,
                null,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );
        Assertions.assertNotNull(video.getDomainEvents());
    }
}
