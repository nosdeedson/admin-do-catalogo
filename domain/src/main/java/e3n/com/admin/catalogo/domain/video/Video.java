package e3n.com.admin.catalogo.domain.video;

import e3n.com.admin.catalogo.domain.AggregateRoot;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.genre.GenreId;
import e3n.com.admin.catalogo.domain.utils.InstantUtils;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;
    private boolean opened;
    private boolean published;
    private Instant createdAt;
    private Instant updatedAt;
    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbNailHalf;
    private AudioVideoMedia trailer;
    private AudioVideoMedia video;
    private Set<CategoryID> categories;
    private Set<GenreId> genres;
    private Set<CastMemberID> castMembers;

    protected Video(
            VideoID videoID, String title, String description, Year launchedAt, double duration, Rating rating, //
            boolean opened, boolean published, Instant createdAt, Instant updatedAt, ImageMedia banner, //
            ImageMedia thumbnail, ImageMedia thumbNailHalf, AudioVideoMedia trailer, AudioVideoMedia video, //
            Set<CategoryID> categories, Set<GenreId> genres, Set<CastMemberID> castMembers) {
        super(videoID);
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = opened;
        this.published = published;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbNailHalf = thumbNailHalf;
        this.trailer = trailer;
        this.video = video;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = castMembers;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new VideoValidator(handler, this).validate();
    }


    public Video update(
            final String title,
            final String description,
            final Year launchedAt,
            final double duration,
            final boolean opened,
            final boolean published,
            final Rating rating,
            final Set<CategoryID> categories,
            final Set<GenreId> genres,
            final Set<CastMemberID> castMembers
    ){
        this.updatedAt = InstantUtils.now();
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setCastMembers(castMembers);
        return this;
    }

    public Video updateBannerMedia(final ImageMedia banner){
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateThumbnailMedia(final  ImageMedia thumbnail){
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateThumbnailHalfMedia(final ImageMedia thumbnailHalf){
        this.thumbNailHalf = thumbnailHalf;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateTrailerMedia(final AudioVideoMedia trailer){
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateVideoMedia(final AudioVideoMedia video){
        this.video = video;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public static Video newVideo(
            final String title,
            final String description,
            final Year launchYear,
            final double duration,
            final boolean opened,
            final boolean published,
            final Rating rating,
            final Set<CategoryID> categories,
            final Set<GenreId> genres,
            final Set<CastMemberID> members
    ){
        final var now = InstantUtils.now();
        final var id = VideoID.unique();
        return new Video(
                id,
                title,
                description,
                launchYear,
                duration,
                rating,
                opened,
                published,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                members
        );
    }

    public static Video with(
            final VideoID id,
            final String title,
            final String description,
            final Year launchYear,
            final double duration,
            final boolean opened,
            final boolean published,
            final Rating rating,
            final Instant creationDate,
            final Instant updateDate,
            final ImageMedia banner,
            final ImageMedia thumb,
            final ImageMedia thumbHalf,
            final AudioVideoMedia trailer,
            final AudioVideoMedia video,
            final Set<CategoryID> categories,
            final Set<GenreId> genres,
            final Set<CastMemberID> members
    ){
        return new Video(
                id,
                title,
                description,
                launchYear,
                duration,
                rating,
                opened,
                published,
                creationDate,
                updateDate,
                banner,
                thumb,
                thumbHalf,
                trailer,
                video,
                categories,
                genres,
                members
        );
    }

    public static Video with(final Video video){
        return  new Video(
                video.id,
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt(),
                video.getDuration(),
                video.getRating(),
                video.isOpened(),
                video.isPublished(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                video.getBanner().orElse(null),
                video.getThumbnail().orElse(null),
                video.getThumbNailHalf().orElse(null),
                video.getTrailer().orElse(null),
                video.getVideo().orElse(null),
                new HashSet<>(video.getCategories()),
                new HashSet<>(video.getGenres()),
                new HashSet<>(video.getCastMembers())

        );
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbNailHalf() {
        return Optional.ofNullable(thumbNailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(this.video);
    }

    public Set<CategoryID> getCategories() {
        return categories;
    }

    public Set<GenreId> getGenres() {
        return genres;
    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers;
    }

    public void setCategories(Set<CategoryID> categories) {
        this.categories = categories;
    }

    public void setGenres(Set<GenreId> genres) {
        this.genres = genres;
    }

    public void setCastMembers(Set<CastMemberID> castMembers) {
        this.castMembers = castMembers;
    }

    public Video processing(final VideoMediaType aType) {
        if (VideoMediaType.VIDEO == aType) {
            getVideo()
                    .ifPresent(media -> updateVideoMedia(media.processing()));
        } else if (VideoMediaType.TRAILER == aType) {
            getTrailer()
                    .ifPresent(media -> updateTrailerMedia(media.processing()));
        }

        return this;
    }

    public Video completed(final VideoMediaType aType, final String encodedPath) {
        if (VideoMediaType.VIDEO == aType) {
            getVideo()
                    .ifPresent(media -> updateVideoMedia(media.completed(encodedPath)));
        } else if (VideoMediaType.TRAILER == aType) {
            getTrailer()
                    .ifPresent(media -> updateTrailerMedia(media.completed(encodedPath)));
        }

        return this;
    }
}
