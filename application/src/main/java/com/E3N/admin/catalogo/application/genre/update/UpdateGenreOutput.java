package com.E3N.admin.catalogo.application.genre.update;

import com.E3N.admin.catalogo.domain.genre.Genre;

public record UpdateGenreOutput(String id) {
    public static UpdateGenreOutput from(Genre genre){
        return new UpdateGenreOutput(genre.getId().getValue());
    }
}
