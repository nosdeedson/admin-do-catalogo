package e3n.com.admin.catalogo.application.video.update;

import e3n.com.admin.catalogo.domain.Identifier;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.DomainException;
import e3n.com.admin.catalogo.domain.exceptions.InternalErrorException;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.genre.GenreId;
import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;
import e3n.com.admin.catalogo.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase{

    private final VideoGateway videoGateway;

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    private final CastMemberGateway memberGateway;

    private final MediaResourceGateway resourceGateway;

    public DefaultUpdateVideoUseCase(VideoGateway videoGateway, CategoryGateway categoryGateway,
                                     GenreGateway genreGateway, CastMemberGateway memberGateway, MediaResourceGateway resourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.resourceGateway = Objects.requireNonNull(resourceGateway);
    }

    @Override
    public UpdateVideoOutput execute(UpdateVideoCommand command) {

        final var videoId = VideoID.from(command.id());
        final var rating = Rating.of(command.rating()).orElse(null);
        final var launched = command.launchedAt() != null ? Year.of(command.launchedAt()) : null;
        final var categories = toIdentifier(command.categories(), CategoryID::from);
        final var genres = toIdentifier(command.genres(), GenreId::from);
        final var members = toIdentifier(command.castMembers(), CastMemberID::from);

        final var video = this.videoGateway.findById(videoId).orElseThrow(notFoundException(videoId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateCastMembers(members));
        video.update(command.title(), command.description(), launched, command.duration(), command.opened(),
                command.published(), rating, categories, genres, members);

        video.validate(notification);
        if(notification.hasError()){
            throw new NotificationException("Could not update Aggregate video", notification);
        }
        final var updated = update(command, video);
        return UpdateVideoOutput.from(updated);

    }

    private Video update(final UpdateVideoCommand command, final Video video){
        final var id = video.getId();

        try {
            final var videoMedia = command.getVideo()
                    .map(it -> this.resourceGateway.storeAudioVideo(id, VideoResource.with(VideoMediaType.VIDEO, it)))
                    .orElse(null);

            final var trailer = command.getTrailer()
                    .map(it -> this.resourceGateway.storeAudioVideo(id, VideoResource.with(VideoMediaType.TRAILER, it)))
                    .orElse(null);

            final var banner = command.getBanner()
                    .map(it -> this.resourceGateway.storeImage(id, VideoResource.with(VideoMediaType.BANNER, it))).orElse(null);

            final var thumbnail = command.getThumbnail()
                    .map(it-> resourceGateway.storeImage(id, VideoResource.with(VideoMediaType.THUMBNAIL, it))).orElse(null);

            final var thumbnailHalf = command.getThumbnailHalf()
                    .map(it-> resourceGateway.storeImage(id, VideoResource.with(VideoMediaType.THUMBNAIL_HALF, it))).orElse(null);

            video.updateVideoMedia(videoMedia)
                    .updateThumbnailMedia(thumbnail)
                    .updateBannerMedia(banner)
                    .updateThumbnailHalfMedia(thumbnailHalf)
                    .updateTrailerMedia(trailer);

            return this.videoGateway.update(video);

        } catch (Throwable t){
            throw InternalErrorException.with( "An error on create video was observed [videoId:%s]".formatted(id.getValue()), t);
        }

    }

    private Supplier<DomainException> notFoundException(final VideoID id){
        return () -> NotFoundException.with(Video.class, id);
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids){
        return validateAggreagate("categories", ids, categoryGateway::existByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreId> ids){
        return validateAggreagate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateCastMembers(final Set<CastMemberID> ids){
        return validateAggreagate("castMembers", ids, memberGateway::existsByIds);
    }

    private <T extends Identifier>ValidationHandler validateAggreagate(
            final String aggregate,
            final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds
    ){
        final var notification = Notification.create();
        if (ids  == null || ids.isEmpty()){
            return notification;
        }

        final var retrieveIds = existsByIds.apply(ids);

        if (ids.size() != retrieveIds.size()){
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrieveIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }
        return  notification;
    }
    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper){
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }
}
