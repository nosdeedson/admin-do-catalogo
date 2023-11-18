package e3n.com.admin.catalogo.domain.video;

import e3n.com.admin.catalogo.domain.Identifier;
import e3n.com.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;

public class VideoID extends Identifier {

    private final String value;

    private VideoID(final String value){
        this.value = Objects.requireNonNull(value);
    }

    public static VideoID from(final String id){
        return new VideoID(id);
    }

    public static VideoID unique(){
        return VideoID.from(IdUtils.uuid());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoID videoID = (VideoID) o;
        return Objects.equals(getValue(), videoID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
