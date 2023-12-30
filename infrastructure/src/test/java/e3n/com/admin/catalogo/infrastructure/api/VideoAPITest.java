package e3n.com.admin.catalogo.infrastructure.api;


import com.E3N.admin.catalogo.application.video.create.CreateVideoCommand;
import com.E3N.admin.catalogo.application.video.create.CreateVideoOutput;
import com.E3N.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.E3N.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.E3N.admin.catalogo.application.video.media.get.GetMediaCommand;
import com.E3N.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.E3N.admin.catalogo.application.video.media.get.MediaOutput;
import com.E3N.admin.catalogo.application.video.media.upload.UploadMediaCommand;
import com.E3N.admin.catalogo.application.video.media.upload.UploadMediaOutput;
import com.E3N.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.E3N.admin.catalogo.application.video.retrieve.list.ListVideoUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.list.VideoListOutput;
import com.E3N.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.E3N.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.E3N.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.validation.Error;
import com.E3N.admin.catalogo.domain.video.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import e3n.com.admin.catalogo.ControllerTest;
import e3n.com.admin.catalogo.Fixture;
import e3n.com.admin.catalogo.infrastructure.video.models.CreateVideoRequest;
import e3n.com.admin.catalogo.infrastructure.video.models.UpdateVideoRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerTest(controllers = VideoApi.class)
public class VideoAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @MockBean
    private GetMediaUseCase getMediaUseCase;

    @MockBean
    private ListVideoUseCase listVideoUseCase;

    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockBean
    private UploadMediaUseCase uploadMediaUseCase;

    @Test
    public void testInjections() {
        Assertions.assertNotNull(mvc);
        Assertions.assertNotNull(mapper);
        Assertions.assertNotNull(createVideoUseCase);
        Assertions.assertNotNull(getVideoByIdUseCase);
        Assertions.assertNotNull(getMediaUseCase);
        Assertions.assertNotNull(listVideoUseCase);
        Assertions.assertNotNull(updateVideoUseCase);
        Assertions.assertNotNull(updateVideoUseCase);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateFull_shouldReturnAnId() throws Exception {
        final var eva = Fixture.CastMembers.eva();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.description();
        final var expectedLaunchedyear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(eva.getId().getValue());

        final var expectedVideo = new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());

        final var expectedTrailer = new MockMultipartFile("trailer_file", "trailer.mp4", "trailer/mp4", "TRAILER".getBytes());

        final var expectedBanner = new MockMultipartFile("banner_file", "banner.mp4", "banner/mp4", "BANNER".getBytes());

        final var expectedThumbnail = new MockMultipartFile("thumbnail_file", "thumbnail.mp4", " thumbnail/mp4", "THUMBNAIL".getBytes());

        final var expectedThumbnailHalf = new MockMultipartFile("thumbnail_half_file", "thumbnailHalf.mp4", " thumbnailHalf/mp4", "THUMBNAILHALF".getBytes());

        Mockito.when(createVideoUseCase.execute(Mockito.any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var request = MockMvcRequestBuilders.multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumbnail)
                .file(expectedThumbnailHalf)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchedyear))
                .param("duration", String.valueOf(expectedDuration))
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("cast_members_id", eva.getId().getValue())
                .param("categories_id", aulas.getId().getValue())
                .param("genres_id", tech.getId().getValue())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        final var captor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        Mockito.verify(createVideoUseCase).execute(captor.capture());

        final var result = captor.getValue();

        Assertions.assertEquals(expectedTitle, result.title());
        Assertions.assertEquals(expectedDescription, result.description());
        Assertions.assertEquals(expectedLaunchedyear, Year.of(result.launchedAt()));
        Assertions.assertEquals(expectedDuration, result.duration());
        Assertions.assertEquals(expectedOpened, result.opened());
        Assertions.assertEquals(expectedPublished, result.published());
        Assertions.assertEquals(expectedRating.getName(), result.rating());
        Assertions.assertEquals(expectedCategories, result.categories());
        Assertions.assertEquals(expectedGenres, result.genres());
        Assertions.assertEquals(expectedMembers, result.members());

        Assertions.assertEquals(expectedVideo.getOriginalFilename(), result.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.getOriginalFilename(), result.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.getOriginalFilename(), result.getBanner().get().name());
        Assertions.assertEquals(expectedThumbnail.getOriginalFilename(), result.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbnailHalf.getOriginalFilename(), result.getThumbnailHalf().get().name());

    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateFull_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is required";
        Mockito.when(createVideoUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.multipart("/videos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsCreatePartial_shouldReturnId() throws Exception {
        final var eva = Fixture.CastMembers.eva();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.description();
        final var expectedLaunchedyear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(eva.getId().getValue());

        Mockito.when(createVideoUseCase.execute(Mockito.any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        final var command = new CreateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchedyear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        final var request = MockMvcRequestBuilders.post("/videos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));
        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        final var captor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        Mockito.verify(createVideoUseCase).execute(captor.capture());

        final var result = captor.getValue();

        Assertions.assertEquals(expectedTitle, result.title());
        Assertions.assertEquals(expectedDescription, result.description());
        Assertions.assertEquals(expectedLaunchedyear, Year.of(result.launchedAt()));
        Assertions.assertEquals(expectedDuration, result.duration());
        Assertions.assertEquals(expectedOpened, result.opened());
        Assertions.assertEquals(expectedPublished, result.published());
        Assertions.assertEquals(expectedRating.getName(), result.rating());
        Assertions.assertEquals(expectedCategories, result.categories());
        Assertions.assertEquals(expectedGenres, result.genres());
        Assertions.assertEquals(expectedMembers, result.members());

        Assertions.assertTrue(result.getVideo().isEmpty());
        Assertions.assertTrue(result.getTrailer().isEmpty());
        Assertions.assertTrue(result.getBanner().isEmpty());
        Assertions.assertTrue(result.getThumbnail().isEmpty());
        Assertions.assertTrue(result.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenAnEmptyBody_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var request = MockMvcRequestBuilders.post("/videos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        final var response = this.mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreatePartial_shouldReturnError() throws Exception {
        final var expectedErrorMessage = "title is requires";
        Mockito.when(createVideoUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/videos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "hello world"
                        }
                        """);
        final var response = this.mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnVideo() throws Exception {
        final var eva = Fixture.CastMembers.eva();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.description();
        final var expectedLaunchedyear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(eva.getId());

        final var expectedVideo = Fixture.Videos.audioVideoMedia(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideoMedia(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var video = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedyear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                ).updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = video.getId().getValue();

        Mockito.when(getVideoByIdUseCase.execute(Mockito.any()))
                .thenReturn(VideoOutput.from(video));

        final var request = MockMvcRequestBuilders.get("/videos/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.equalTo(expectedTitle)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year_launched", Matchers.equalTo(expectedLaunchedyear.getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration", Matchers.equalTo(expectedDuration)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.opened", Matchers.equalTo(expectedOpened)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.published", Matchers.equalTo(expectedPublished)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", Matchers.equalTo(expectedRating.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(video.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(video.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banner.id", Matchers.equalTo(expectedBanner.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banner.name", Matchers.equalTo(expectedBanner.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banner.location", Matchers.equalTo(expectedBanner.location())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.banner.checksum", Matchers.equalTo(expectedBanner.checksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail.id", Matchers.equalTo(expectedThumb.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail.name", Matchers.equalTo(expectedThumb.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail.location", Matchers.equalTo(expectedThumb.location())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnail.checksum", Matchers.equalTo(expectedThumb.checksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnailHalf.id", Matchers.equalTo(expectedThumbHalf.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnailHalf.name", Matchers.equalTo(expectedThumbHalf.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnailHalf.location", Matchers.equalTo(expectedThumbHalf.location())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.thumbnailHalf.checksum", Matchers.equalTo(expectedThumbHalf.checksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.id", Matchers.equalTo(expectedVideo.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.name", Matchers.equalTo(expectedVideo.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.location", Matchers.equalTo(expectedVideo.rawLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.checksum", Matchers.equalTo(expectedVideo.checksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video.encoded_location", Matchers.equalTo(expectedVideo.encodedLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.id", Matchers.equalTo(expectedTrailer.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.name", Matchers.equalTo(expectedTrailer.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.location", Matchers.equalTo(expectedTrailer.rawLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.checksum", Matchers.equalTo(expectedTrailer.checksum())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trailer.encoded_location", Matchers.equalTo(expectedTrailer.encodedLocation())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories_id", Matchers.equalTo(new ArrayList(expectedCategories.stream().map(CategoryID::getValue).toList()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.members_id", Matchers.equalTo(new ArrayList(expectedMembers.stream().map(CastMemberID::getValue).toList()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genres_id", Matchers.equalTo(new ArrayList(expectedGenres.stream().map(GenreId::getValue).toList()))));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetById_shouldReturnNotFound() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());
        Mockito.when(getVideoByIdUseCase.execute(expectedId.getValue()))
                .thenThrow(NotFoundException.with(Video.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/videos/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() throws Exception {
        final var eva = Fixture.CastMembers.eva();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.description();
        final var expectedLaunchedyear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(eva.getId().getValue());

        final var command = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchedyear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        Mockito.when(updateVideoUseCase.execute(Mockito.any()))
                .thenReturn(UpdateVideoOutput.from(expectedId.getValue()));

        final var request = MockMvcRequestBuilders.put("/videos/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));
        final var captor = ArgumentCaptor.forClass(UpdateVideoCommand.class);
        Mockito.verify(updateVideoUseCase).execute(captor.capture());
        final var result = captor.getValue();
        Assertions.assertEquals(expectedId.getValue(), result.id());
        Assertions.assertEquals(expectedTitle, result.title());
        Assertions.assertEquals(expectedDescription, result.description());
        Assertions.assertEquals(expectedLaunchedyear.getValue(), result.launchedAt());
        Assertions.assertEquals(expectedDuration, result.duration());
        Assertions.assertEquals(expectedOpened, result.opened());
        Assertions.assertEquals(expectedPublished, result.published());
        Assertions.assertEquals(expectedRating.getName(), result.rating());
        Assertions.assertEquals(expectedCategories, result.categories());
        Assertions.assertEquals(expectedGenres, result.genres());
        Assertions.assertEquals(expectedMembers, result.castMembers());

        Assertions.assertTrue(result.getVideo().isEmpty());
        Assertions.assertTrue(result.getTrailer().isEmpty());
        Assertions.assertTrue(result.getThumbnail().isEmpty());
        Assertions.assertTrue(result.getThumbnailHalf().isEmpty());
        Assertions.assertTrue(result.getBanner().isEmpty());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsUpdateVideo_shouldReturnNotification() throws Exception {
        final var eva = Fixture.CastMembers.eva();
        final var aulas = Fixture.Categories.aulas();
        final var tech = Fixture.Genres.tech();
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedTitle = "";
        final var expectedDescription = Fixture.description();
        final var expectedLaunchedyear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.rating();
        final var expectedCategories = Set.of(aulas.getId().getValue());
        final var expectedGenres = Set.of(tech.getId().getValue());
        final var expectedMembers = Set.of(eva.getId().getValue());

        final var command = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchedyear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        Mockito.when(updateVideoUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.put("/videos/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateVideoUseCase).execute(Mockito.any());
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        final var expectedId = VideoID.unique();
        Mockito.doNothing()
                .when(deleteVideoUseCase).execute(Mockito.any());

        final var request = MockMvcRequestBuilders.delete("/videos/{id}", expectedId.getValue());

        final var response = mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteVideoUseCase).execute(Mockito.any());
    }

    @Test
    public void givenValidParams_whenCallsListVideos_shouldReturnPagination() throws Exception {
        final var preview = new VideoPreview(Fixture.video());
        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Algo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedMembers = "eva";
        final var expectedGenres = "genre";
        final var expectedCategories = "cat";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(VideoListOutput.from(preview));

        Mockito.when(listVideoUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/videos/")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("cast_members_ids", expectedMembers)
                .queryParam("categories_ids", expectedCategories)
                .queryParam("genres_ids", expectedGenres)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(preview.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].title", Matchers.equalTo(preview.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(preview.description())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(preview.createdAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].updated_at", Matchers.equalTo(preview.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        Mockito.verify(listVideoUseCase).execute(captor.capture());

        final var result = captor.getValue();

        Assertions.assertEquals(expectedPage, result.page());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTerms, result.terms());
        Assertions.assertEquals(expectedSort, result.sort());
        Assertions.assertEquals(expectedDirection, result.direction());
        Assertions.assertEquals(Set.of(CastMemberID.from(expectedMembers)), result.castMembers());
        Assertions.assertEquals(Set.of(CategoryID.from(expectedCategories)), result.categories());
        Assertions.assertEquals(Set.of(GenreId.from(expectedGenres)), result.genres());

    }

    @Test
    public void givenEmptyParams_whenCallsListVideosWithDefaultValues_shouldReturnPagination() throws Exception {
        final var preview = new VideoPreview(Fixture.video());
        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "sort";
        final var expectedDirection = "asc";
        final CastMemberID expectedMembers = null;
        final GenreId expectedGenres = null;
        final CategoryID expectedCategories = null;
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(VideoListOutput.from(preview));

        Mockito.when(listVideoUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));
        final var request = MockMvcRequestBuilders.get("/videos/")
                .accept(MediaType.APPLICATION_JSON);
        final var response = mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(preview.id())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].title", Matchers.equalTo(preview.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(preview.description())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(preview.createdAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].updated_at", Matchers.equalTo(preview.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        Mockito.verify(listVideoUseCase).execute(captor.capture());

        final var result = captor.getValue();

        Assertions.assertEquals(expectedPage, result.page());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(expectedTerms, result.terms());
        Assertions.assertEquals(expectedSort, result.sort());
        Assertions.assertEquals(expectedDirection, result.direction());
        Assertions.assertEquals(Set.<CastMemberID>of(), result.castMembers());
        Assertions.assertEquals(Set.<CategoryID>of(), result.categories());
        Assertions.assertEquals(Set.<GenreId>of(), result.genres());

    }

    @Test
    public void givenAValidVideoIdAndFileType_whenCallsGetMediaById_shouldReturnContent() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedMediaType = VideoMediaType.VIDEO;
        final var expectedReosource = Fixture.Videos.resource(expectedMediaType);
        final var expectedMedia = new MediaOutput(expectedReosource.content(), expectedReosource.contentType(), expectedReosource.name());
        Mockito.when(getMediaUseCase.execute(Mockito.any()))
                .thenReturn(expectedMedia);

        final var request = MockMvcRequestBuilders.get("/videos/{id}/medias/{type}", expectedId.getValue(), expectedMediaType.name());
        final var response = this.mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, expectedMedia.contentType()))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(expectedMedia.content().length)))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(expectedReosource.name())))
                .andExpect(MockMvcResultMatchers.content().bytes(expectedMedia.content()));

        final var captor = ArgumentCaptor.forClass(GetMediaCommand.class);
        Mockito.verify(getMediaUseCase).execute(captor.capture());
        final var result = captor.getValue();
        Assertions.assertEquals(expectedId.getValue(), result.videoId());
        Assertions.assertEquals(expectedMediaType.name(), result.mediaType());
    }

    @Test
    public void givenAValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);

        final var expectedVideo = new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        Mockito.when(uploadMediaUseCase.execute(Mockito.any()))
                .thenReturn(new UploadMediaOutput(expectedId.getValue(), expectedType));

        final var request = MockMvcRequestBuilders.multipart("/videos/{id}/medias/{type}", expectedId.getValue(), expectedType)
                .file(expectedVideo)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response =this.mvc.perform(request);
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/videos/%s/medias/%s".formatted(expectedId.getValue(), expectedType.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.video_id", Matchers.equalTo(expectedId.getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.media_type", Matchers.equalTo(expectedType.name())));

        final var captor = ArgumentCaptor.forClass(UploadMediaCommand.class);

        Mockito.verify(uploadMediaUseCase).execute(captor.capture());
        final var result = captor.getValue();
        Assertions.assertEquals(expectedId.getValue(), result.videoId());
        Assertions.assertEquals(expectedResource.content(), result.resource().resource().content());
        Assertions.assertEquals(expectedResource.name(), result.resource().resource().name());
        Assertions.assertEquals(expectedResource.contentType(), result.resource().resource().contentType());
        Assertions.assertEquals(expectedType, result.resource().type());
    }

    @Test
    public void givenAnInvalidMediaType_whenCallsUploadMedia_shouldReturnError() throws Exception {
        final var expectedId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedVideo = new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        final var request = MockMvcRequestBuilders.multipart("/videos/{id}/medias/INVALID", expectedId.getValue(), "test")
                .file(expectedVideo)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Invalid media type: INVALID")));
    }
}
