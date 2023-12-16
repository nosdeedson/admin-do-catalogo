package com.E3N.admin.catalogo.application.genre.create;

import e3n.com.admin.catalogo.domain.genre.Genre;

public record CreateGenreOutput(
        String id
) {

    public static CreateGenreOutput from(final String id){
        return new CreateGenreOutput(id);
    }

    public static CreateGenreOutput from(final Genre genre){
        return new CreateGenreOutput(genre.getId().getValue());
    }
}
