package e3n.com.admin.catalogo.infrastructure.video.persistence;

import e3n.com.admin.catalogo.domain.video.AudioVideoMedia;
import e3n.com.admin.catalogo.domain.video.MediaStatus;

import javax.persistence.*;

@Entity(name = "AudioVideoMedia")
@Table(name = "video_video_media")
public class AudioVideoMediaJpaEntity {

    @Id
    private String id;

    @Column(name = "checksum", nullable = false)
    private String checksum;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "encoded_path", nullable = false)
    private String encodedPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_status", nullable = false)
    private MediaStatus status;

    public AudioVideoMediaJpaEntity() {
    }

    private AudioVideoMediaJpaEntity(String id, String checksum, String name, String filePath, String encodedPath,
                                     MediaStatus status) {
        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.filePath = filePath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static AudioVideoMediaJpaEntity from(final AudioVideoMedia media) {
        return new AudioVideoMediaJpaEntity(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status()
        );
    }

    public AudioVideoMedia toDomain() {
        return AudioVideoMedia.with(
                getId(),
                getChecksum(),
                getName(),
                getFilePath(),
                getEncodedPath(),
                getStatus()
        );
    }

    public AudioVideoMediaJpaEntity setId(String id) {
        this.id = id;
        return this;
    }

    public AudioVideoMediaJpaEntity setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public AudioVideoMediaJpaEntity setName(String name) {
        this.name = name;
        return this;
    }

    public AudioVideoMediaJpaEntity setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public AudioVideoMediaJpaEntity setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
        return this;
    }

    public AudioVideoMediaJpaEntity setStatus(MediaStatus status) {
        this.status = status;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getChecksum() {
        return checksum;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public MediaStatus getStatus() {
        return status;
    }

}
