package e3n.com.admin.catalogo.infrastructure.video.persistence;


import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import com.E3N.admin.catalogo.domain.video.Rating;
import com.E3N.admin.catalogo.domain.video.Video;
import com.E3N.admin.catalogo.domain.video.VideoID;

import javax.persistence.*;
import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "Video")
@Table(name = "video")
public class VideoJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    @Column(name = "year_launched", nullable = false)
    private int yearLaunched;

    @Column(name = "opened", nullable = false)
    private boolean opened;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "rating")
    private Rating rating;

    @Column(name = "duration", precision = 2)
    private double duration;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private AudioVideoMediaJpaEntity video;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "trailer_id")
    private AudioVideoMediaJpaEntity trailer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "banner_id")
    private ImageMediaJpaEntity banner;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    private ImageMediaJpaEntity thumbnail;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_half_id")
    private ImageMediaJpaEntity thumbnailHalf;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoCategoryJpaEntity> categories;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoGenreJpaEntity> genres;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoCastMemberJpaEntity> members;

    public VideoJpaEntity() {
    }

    private VideoJpaEntity(String id, String title, String description, int yearLaunched, boolean opened, boolean published, Rating rating, double duration, Instant createdAt, Instant updatedAt, AudioVideoMediaJpaEntity video, AudioVideoMediaJpaEntity trailer, ImageMediaJpaEntity banner, ImageMediaJpaEntity thumbnail, ImageMediaJpaEntity thumbnailHalf) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.yearLaunched = yearLaunched;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.video = video;
        this.trailer = trailer;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.categories = new HashSet<>(3);
        this.genres = new HashSet<>(3);
        this.members = new HashSet<>(3);
    }

    public static VideoJpaEntity from(final Video video){
        final var entity = new VideoJpaEntity(
                video.getId().getValue(),
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt().getValue(),
                video.isOpened(),
                video.isPublished(),
                video.getRating(),
                video.getDuration(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                video.getVideo()
                        .map(AudioVideoMediaJpaEntity::from)
                        .orElse(null),
                video.getTrailer()
                        .map(AudioVideoMediaJpaEntity::from)
                        .orElse(null),
                video.getBanner()
                        .map(ImageMediaJpaEntity::from)
                        .orElse(null),
                video.getThumbnail()
                        .map(ImageMediaJpaEntity::from)
                        .orElse(null),
                video.getThumbNailHalf()
                        .map(ImageMediaJpaEntity::from)
                        .orElse(null)
        );
        video.getCategories().forEach(entity::addCategory);
        video.getCastMembers().forEach(entity::addMembers);
        video.getGenres().forEach(entity::addGenres);
        return entity;
    }

    public Video toAggregate(){
        return Video.with(
                VideoID.from(this.id),
                this.title,
                this.description,
                Year.of(this.yearLaunched),
                this.duration,
                this.opened,
                this.published,
                this.rating,
                this.createdAt,
                this.updatedAt,
                Optional.ofNullable(this.banner)
                        .map(ImageMediaJpaEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(this.thumbnail)
                        .map(ImageMediaJpaEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(thumbnailHalf)
                        .map(ImageMediaJpaEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(this.trailer)
                        .map(AudioVideoMediaJpaEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(this.video)
                        .map(AudioVideoMediaJpaEntity::toDomain)
                        .orElse(null),
                this.categories.stream()
                        .map(it -> CategoryID.from(it.getId().getCategoryId()))
                        .collect(Collectors.toSet()),
                this.genres.stream()
                        .map(it -> GenreId.from(it.getId().getGenreId()))
                        .collect(Collectors.toSet()),
                this.members.stream()
                        .map(it -> CastMemberID.from(it.getId().getCastMemberId()))
                        .collect(Collectors.toSet())
        );
    }

    public void addCategory(final CategoryID id){
        this.categories.add(VideoCategoryJpaEntity.from(this, id));
    }

    public void addMembers(final CastMemberID id){
        this.members.add(VideoCastMemberJpaEntity.from(this, id));
    }

    public void addGenres(final GenreId id){
        this.genres.add(VideoGenreJpaEntity.from(this, id));
    }

    public String getId() {
        return id;
    }

    public VideoJpaEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public VideoJpaEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public VideoJpaEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getYearLaunched() {
        return yearLaunched;
    }

    public VideoJpaEntity setYearLaunched(int yearLaunched) {
        this.yearLaunched = yearLaunched;
        return this;
    }

    public boolean isOpened() {
        return opened;
    }

    public VideoJpaEntity setOpened(boolean opened) {
        this.opened = opened;
        return this;
    }

    public boolean isPublished() {
        return published;
    }

    public VideoJpaEntity setPublished(boolean published) {
        this.published = published;
        return this;
    }

    public Rating getRating() {
        return rating;
    }

    public VideoJpaEntity setRating(Rating rating) {
        this.rating = rating;
        return this;
    }

    public double getDuration() {
        return duration;
    }

    public VideoJpaEntity setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public VideoJpaEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public VideoJpaEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public AudioVideoMediaJpaEntity getVideo() {
        return video;
    }

    public VideoJpaEntity setVideo(AudioVideoMediaJpaEntity video) {
        this.video = video;
        return this;
    }

    public AudioVideoMediaJpaEntity getTrailer() {
        return trailer;
    }

    public VideoJpaEntity setTrailer(AudioVideoMediaJpaEntity trailer) {
        this.trailer = trailer;
        return this;
    }

    public ImageMediaJpaEntity getBanner() {
        return banner;
    }

    public VideoJpaEntity setBanner(ImageMediaJpaEntity banner) {
        this.banner = banner;
        return this;
    }

    public ImageMediaJpaEntity getThumbnail() {
        return thumbnail;
    }

    public VideoJpaEntity setThumbnail(ImageMediaJpaEntity thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public ImageMediaJpaEntity getThumbnailHalf() {
        return thumbnailHalf;
    }

    public VideoJpaEntity setThumbnailHalf(ImageMediaJpaEntity thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        return this;
    }

    public Set<VideoCategoryJpaEntity> getCategories() {
        return categories;
    }

    public VideoJpaEntity setCategories(Set<VideoCategoryJpaEntity> categories) {
        this.categories = categories;
        return this;
    }

    public Set<VideoGenreJpaEntity> getGenres() {
        return genres;
    }

    public VideoJpaEntity setGenres(Set<VideoGenreJpaEntity> genres) {
        this.genres = genres;
        return this;
    }

    public Set<VideoCastMemberJpaEntity> getMembers() {
        return members;
    }

    public VideoJpaEntity setMembers(Set<VideoCastMemberJpaEntity> members) {
        this.members = members;
        return this;
    }
}
