package com.E3N.admin.catalogo.application.genre.delete;

import com.E3N.admin.catalogo.domain.genre.GenreGateway;
import com.E3N.admin.catalogo.domain.genre.GenreId;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase{

    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(String id) {
        this.genreGateway.deleteById(GenreId.from(id));
    }
}
