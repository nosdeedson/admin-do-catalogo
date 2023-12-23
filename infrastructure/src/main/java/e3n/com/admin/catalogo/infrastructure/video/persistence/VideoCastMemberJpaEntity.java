package e3n.com.admin.catalogo.infrastructure.video.persistence;


import com.E3N.admin.catalogo.domain.castmember.CastMemberID;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "VideoCastMember")
@Table(name = "video_cast_member")
public class VideoCastMemberJpaEntity {

    @EmbeddedId
    private VideoCastMemberID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCastMemberJpaEntity() {
    }

    private VideoCastMemberJpaEntity(VideoCastMemberID id, VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCastMemberJpaEntity from(final VideoJpaEntity entity, final CastMemberID castMemberID){
        final var videoCastMemberId = VideoCastMemberID.from(entity.getId(),
                castMemberID.getValue());
        return new VideoCastMemberJpaEntity(videoCastMemberId, entity);
    }

    public VideoCastMemberID getId() {
        return id;
    }

    public VideoCastMemberJpaEntity setId(VideoCastMemberID id) {
        this.id = id;
        return this;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public VideoCastMemberJpaEntity setVideo(VideoJpaEntity video) {
        this.video = video;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(video, that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, video);
    }
}
