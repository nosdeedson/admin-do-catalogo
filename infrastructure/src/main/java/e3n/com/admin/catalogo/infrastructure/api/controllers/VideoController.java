package e3n.com.admin.catalogo.infrastructure.api.controllers;

import com.E3N.admin.catalogo.application.video.create.CreateVideoCommand;
import com.E3N.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.E3N.admin.catalogo.application.video.create.DefaultCreateVideoUseCase;
import com.E3N.admin.catalogo.application.video.delete.DefaultDeleteVideoUseCase;
import com.E3N.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.E3N.admin.catalogo.application.video.media.get.GetMediaCommand;
import com.E3N.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.E3N.admin.catalogo.application.video.media.upload.UploadMediaCommand;
import com.E3N.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.E3N.admin.catalogo.application.video.retrieve.list.ListVideoUseCase;
import com.E3N.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.E3N.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.utils.CollectionsUtils;
import com.E3N.admin.catalogo.domain.validation.Error;
import com.E3N.admin.catalogo.domain.video.Resource;
import com.E3N.admin.catalogo.domain.video.VideoMediaType;
import com.E3N.admin.catalogo.domain.video.VideoResource;
import com.E3N.admin.catalogo.domain.video.VideoSearchQuery;
import e3n.com.admin.catalogo.infrastructure.api.VideoApi;
import e3n.com.admin.catalogo.infrastructure.utils.HashingUtils;
import e3n.com.admin.catalogo.infrastructure.video.models.CreateVideoRequest;
import e3n.com.admin.catalogo.infrastructure.video.models.UpdateVideoRequest;
import e3n.com.admin.catalogo.infrastructure.video.models.VideoListResponse;
import e3n.com.admin.catalogo.infrastructure.video.models.VideoResponse;
import e3n.com.admin.catalogo.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoApi {

    private final CreateVideoUseCase createVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final GetMediaUseCase getMediaUseCase;
    private final ListVideoUseCase listVideoUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(
            CreateVideoUseCase createVideoUseCase,
            DeleteVideoUseCase deleteVideoUseCase,
            GetVideoByIdUseCase getVideoByIdUseCase,
            GetMediaUseCase getMediaUseCase,
            ListVideoUseCase listVideoUseCase,
            UpdateVideoUseCase updateVideoUseCase,
            UploadMediaUseCase uploadMediaUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.listVideoUseCase = Objects.requireNonNull(listVideoUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public ResponseEntity<?> createFull(String title, String description, Integer yearLaunched, Double duration, Boolean opened, Boolean published, String rating, Set<String> categories, Set<String> castMembers, Set<String> genres, MultipartFile videoFile, MultipartFile trailerFile, MultipartFile bannerFile, MultipartFile thumbFile, MultipartFile thumbHalfFile) {
        final var command = CreateVideoCommand.with(title, description, yearLaunched, duration, opened, published, rating, categories, genres, castMembers, resourceOf(videoFile), resourceOf(trailerFile), resourceOf(bannerFile), resourceOf(thumbFile), resourceOf(thumbHalfFile));
        final var output = this.createVideoUseCase.execute(command);
        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(CreateVideoRequest request) {
        final var command = CreateVideoCommand.with(request.title(), request.description(), request.yearLaunched(), request.duration(), request.opened(), request.published(), request.rating(), request.categories(), request.genres(), request.members());
        final var output = this.createVideoUseCase.execute(command);
        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public void deleteById(String id) {
        this.deleteVideoUseCase.execute(id);
    }

    @Override
    public VideoResponse getById(String id) {
        final var output = getVideoByIdUseCase.execute(id);
        return VideoApiPresenter.present(output);
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(String id, String type) {
        final var command = GetMediaCommand.with(id, type);
        final var media = this.getMediaUseCase.execute(command);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(media.contentType()))
                .contentLength(media.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(media.name()))
                .body(media.content());
    }

    @Override
    public Pagination<VideoListResponse> list(String search, int page, int perPage, String sort, String direction, Set<String> members, Set<String> categoriesIds, Set<String> genresIds) {
        final var categories = CollectionsUtils.mapTo(categoriesIds, CategoryID::from);
        final var genres = CollectionsUtils.mapTo(genresIds, GenreId::from);
        final var castMembers = CollectionsUtils.mapTo(members, CastMemberID::from);
        final var query = new VideoSearchQuery(page, perPage, search, sort, direction, castMembers, categories, genres);
        final var results = listVideoUseCase.execute(query);
        return VideoApiPresenter.present(results);
    }

    @Override
    public ResponseEntity<?> update(String id, UpdateVideoRequest request) {
        final var command = UpdateVideoCommand.with(
                id,
                request.title(),
                request.description(),
                request.yearLaunched(),
                request.duration(),
                request.opened(),
                request.published(),
                request.rating(),
                request.categories(),
                request.genres(),
                request.members()
        );

        final var output = updateVideoUseCase.execute(command);
        return ResponseEntity.ok()
                .location(URI.create("/videos/" + output.id()))
                .body(output);
    }

    @Override
    public ResponseEntity<?> uploadMediaByType(String id, String type, MultipartFile media) {
        final var mediaType = VideoMediaType.of(type)
                .orElseThrow(() -> NotificationException.with(new Error("Invalid media type: %s".formatted(type))));
        final var resource = VideoResource.with(mediaType, resourceOf(media));
        final var command = UploadMediaCommand.with(id, resource);
        final var output = uploadMediaUseCase.execute(command);
        return ResponseEntity.created(URI.create("/videos/%s/medias/%s".formatted(id, type)))
                .body(VideoApiPresenter.present(output));
    }

    private Resource resourceOf(MultipartFile file) {
        if (file == null) {
            return null;
        }
        try {
            return Resource.with(file.getBytes(), HashingUtils.checksum(file.getBytes()), file.getContentType(), file.getOriginalFilename());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
