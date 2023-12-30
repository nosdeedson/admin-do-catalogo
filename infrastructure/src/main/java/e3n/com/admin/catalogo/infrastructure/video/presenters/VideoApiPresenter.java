package e3n.com.admin.catalogo.infrastructure.video.presenters;

import com.E3N.admin.catalogo.application.video.media.upload.UploadMediaOutput;
import com.E3N.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.E3N.admin.catalogo.application.video.retrieve.list.VideoListOutput;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.video.AudioVideoMedia;
import com.E3N.admin.catalogo.domain.video.ImageMedia;
import e3n.com.admin.catalogo.infrastructure.video.models.*;

public interface VideoApiPresenter {

    static VideoResponse present(final VideoOutput output){
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createAt(),
                output.udpatedAt(),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                present(output.video()),
                present(output.trailer()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia media){
        if (media == null){
            return  null;
        }
        return new AudioVideoMediaResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name()
        );
    }

    static ImageMediaResponse present(final ImageMedia image){
        if (image == null){
            return null;
        }
        return new ImageMediaResponse(image.id(), image.checksum(), image.name(), image.location());
    }

    static VideoListResponse present(final VideoListOutput output){
        return new VideoListResponse(output.id(), output.title(), output.description(), output.createdAt(), output.updatedAt());
    }

    static Pagination<VideoListResponse> present(final Pagination<VideoListOutput> output){
        return output.map(VideoApiPresenter::present);
    }

    static UploadMediaResponse present(UploadMediaOutput output) {
        return new UploadMediaResponse(output.videoId(), output.type().name());
    }
}
