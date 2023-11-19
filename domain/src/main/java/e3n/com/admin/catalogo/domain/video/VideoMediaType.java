package e3n.com.admin.catalogo.domain.video;

import java.util.Arrays;
import java.util.Optional;

public enum VideoMediaType {
    VIDEO,
    TRAILER,
    BANNER,
    THUMBNAIL,
    THUMBNAIL_HALF;


    public static Optional<VideoMediaType> of(final String value){
        return Arrays.stream(VideoMediaType.values())
                .filter(it -> it.name().equals(value))
                .findFirst();
    }
}
