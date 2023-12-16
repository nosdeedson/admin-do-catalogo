package com.E3N.admin.catalogo.domain.genre;

import com.E3N.admin.catalogo.domain.Identifier;
import com.E3N.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;

public class GenreId extends Identifier {

    private final String value;

    private GenreId(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static GenreId unique(){
        return GenreId.from(IdUtils.uuid());
    }

    public static GenreId from(final String id){
        return new GenreId(id.trim());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreId genreId = (GenreId) o;
        return Objects.equals(value, genreId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
