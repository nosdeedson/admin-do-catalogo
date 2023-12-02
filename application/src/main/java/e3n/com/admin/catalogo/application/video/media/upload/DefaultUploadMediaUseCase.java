package e3n.com.admin.catalogo.application.video.media.upload;

import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.video.MediaResourceGateway;
import e3n.com.admin.catalogo.domain.video.Video;
import e3n.com.admin.catalogo.domain.video.VideoGateway;
import e3n.com.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultUploadMediaUseCase extends UploadMediaUseCase {

    private final MediaResourceGateway resourceGateway;

    private final VideoGateway videoGateway;

    public DefaultUploadMediaUseCase(MediaResourceGateway resourceGateway, VideoGateway videoGateway) {
        this.resourceGateway = Objects.requireNonNull(resourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }


    @Override
    public UploadMediaOutput execute(UploadMediaCommand command) {
        final var videoId = VideoID.from(command.videoId());
        final var resource = command.resource();

        final var video = videoGateway.findById(videoId)
                .orElseThrow(() -> NotFoundException.with(Video.class, videoId));

        switch (resource.type()){
            case VIDEO -> video.updateVideoMedia(resourceGateway.storeAudioVideo(videoId,resource));
            case BANNER -> video.updateBannerMedia(resourceGateway.storeImage(videoId, resource));
            case TRAILER -> video.updateTrailerMedia(resourceGateway.storeAudioVideo(videoId, resource));
            case THUMBNAIL -> video.updateThumbnailMedia(resourceGateway.storeImage(videoId, resource));
            case THUMBNAIL_HALF -> video.updateThumbnailHalfMedia(resourceGateway.storeImage(videoId, resource));
        }
        final var updatedVideo = videoGateway.update(video);
        return UploadMediaOutput.with(updatedVideo, resource.type());
    }
}
