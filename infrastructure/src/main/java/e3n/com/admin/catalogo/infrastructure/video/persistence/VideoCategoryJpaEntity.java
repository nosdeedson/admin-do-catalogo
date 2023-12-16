package e3n.com.admin.catalogo.infrastructure.video.persistence;

import com.E3N.admin.catalogo.domain.category.CategoryID;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "VideoCategory")
@Table(name = "video_category")
public class VideoCategoryJpaEntity {

    @EmbeddedId
    private VideoCategoryID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCategoryJpaEntity() {
    }

    private VideoCategoryJpaEntity(VideoCategoryID id, VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCategoryJpaEntity from(final VideoJpaEntity entity, final CategoryID id){
        final var videoCategoryId = VideoCategoryID.from(entity.getId(), id.getValue());
        return new VideoCategoryJpaEntity(videoCategoryId, entity);
    }

    public VideoCategoryID getId() {
        return id;
    }

    public VideoCategoryJpaEntity setId(VideoCategoryID id) {
        this.id = id;
        return this;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public VideoCategoryJpaEntity setVideo(VideoJpaEntity video) {
        this.video = video;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCategoryJpaEntity that = (VideoCategoryJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(video, that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, video);
    }
}
