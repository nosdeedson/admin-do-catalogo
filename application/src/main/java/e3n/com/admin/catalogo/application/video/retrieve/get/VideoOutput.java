package e3n.com.admin.catalogo.application.video.retrieve.get;

import e3n.com.admin.catalogo.domain.Identifier;
import e3n.com.admin.catalogo.domain.utils.CollectionsUtils;
import e3n.com.admin.catalogo.domain.video.AudioVideoMedia;
import e3n.com.admin.catalogo.domain.video.ImageMedia;
import e3n.com.admin.catalogo.domain.video.Rating;
import e3n.com.admin.catalogo.domain.video.Video;

import java.time.Instant;
import java.time.Year;
import java.util.Set;

public record VideoOutput(
        String id,
        Instant createAt,
        Instant udpatedAt,
        String title,
        String description,
        int launchedAt,
        double duration,
        boolean opened,
        boolean published,
        Rating rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf,
        AudioVideoMedia video,
        AudioVideoMedia trailer
) {

    public static VideoOutput from(final Video video) {
        return new VideoOutput(
                video.getId().getValue(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt().getValue(),
                video.getDuration(),
                video.isOpened(),
                video.isPublished(),
                video.getRating(),
                CollectionsUtils.mapTo(video.getCategories(), Identifier::getValue),
                CollectionsUtils.mapTo(video.getGenres(), Identifier::getValue),
                CollectionsUtils.mapTo(video.getCastMembers(), Identifier::getValue),
                video.getBanner().orElse(null),
                video.getThumbnail().orElse(null),
                video.getThumbNailHalf().orElse(null),
                video.getVideo().orElse(null),
                video.getTrailer().orElse(null)
        );
    }
}
