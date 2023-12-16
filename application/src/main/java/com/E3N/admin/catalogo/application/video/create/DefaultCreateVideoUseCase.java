package com.E3N.admin.catalogo.application.video.create;

import com.E3N.admin.catalogo.domain.video.*;
import com.E3N.admin.catalogo.domain.Identifier;
import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.InternalErrorException;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.genre.GenreGateway;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import com.E3N.admin.catalogo.domain.validation.Error;
import com.E3N.admin.catalogo.domain.validation.ValidationHandler;
import com.E3N.admin.catalogo.domain.validation.handler.Notification;
import e3n.com.admin.catalogo.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway memberGateway;

    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public DefaultCreateVideoUseCase(
            CategoryGateway categoryGateway,
            GenreGateway genreGateway,
            CastMemberGateway memberGateway,
            MediaResourceGateway mediaResourceGateway,
            VideoGateway videoGateway) {
        this.categoryGateway = categoryGateway;
        this.genreGateway = genreGateway;
        this.memberGateway = memberGateway;
        this.mediaResourceGateway = mediaResourceGateway;
        this.videoGateway = videoGateway;
    }

    @Override
    public CreateVideoOutput execute(CreateVideoCommand createVideoCommand) {

        final var rating = Rating.of(createVideoCommand.rating()).orElse(null);
        final var lauchedYear = createVideoCommand.launchedAt() != null ? Year.of(createVideoCommand.launchedAt()) : null;
        final var categories = toIdentifier(createVideoCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(createVideoCommand.genres(), GenreId::from);
        final var members = toIdentifier(createVideoCommand.members(), CastMemberID::from);
        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

        final var video = Video.newVideo(
                createVideoCommand.title(),
                createVideoCommand.description(),
                lauchedYear,
                createVideoCommand.duration(),
                createVideoCommand.opened(),
                createVideoCommand.published(),
                rating,
                categories,
                genres,
                members
        );

        video.validate(notification);

        if (notification.hasError()){
            throw new NotificationException("Could not create Aggregate Video", notification);
        }
        final var result = create(createVideoCommand, video);
        return CreateVideoOutput.from(result);
    }

    private Video create(CreateVideoCommand command, Video video){
        final var id = video.getId();
        try {
            final var videoMedia = command.getVideo()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(id, VideoResource.with(VideoMediaType.VIDEO, it)))
                    .orElse(null);

            final var trailer = command.getTrailer()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(id, VideoResource.with(VideoMediaType.TRAILER, it)))
                    .orElse(null);
            final var banner = command.getBanner()
                    .map(it -> this.mediaResourceGateway.storeImage(id, VideoResource.with(VideoMediaType.BANNER, it)))
                    .orElse(null);
            final var thumbnail = command.getThumbnail()
                    .map(it -> this.mediaResourceGateway.storeImage(id, VideoResource.with(VideoMediaType.THUMBNAIL, it)))
                    .orElse(null);
            final var thumbnailHalf = command.getThumbnail()
                    .map(it -> this.mediaResourceGateway.storeImage(id, VideoResource.with(VideoMediaType.THUMBNAIL_HALF, it)))
                    .orElse(null);

            video.updateVideoMedia(videoMedia);
            video.updateBannerMedia(banner);
            video.updateTrailerMedia(trailer);
            video.updateThumbnailMedia(thumbnail);
            video.updateThumbnailHalfMedia(thumbnailHalf);
            return this.videoGateway.create(video);
        } catch ( Throwable throwable){
            this.mediaResourceGateway.clearReources(id);
            throw InternalErrorException.with(
                    "An error on create video was observed [videoId:%s]".formatted(id.getValue()), throwable
            );
        }
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> ids) {
        return validateAggregate("castMembers", ids, memberGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreId> ids) {
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, categoryGateway::existByIds);
    }


    private <T extends Identifier> ValidationHandler validateAggregate(
            final String aggregate,
            final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds
    ) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }

        return notification;
    }


    private <T> Set<T> toIdentifier(Set<String> ids, final Function<String, T> mapper) {
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
