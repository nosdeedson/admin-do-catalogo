package com.E3N.admin.catalogo.application.genre.retrieve.get;

import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.genre.Genre;
import com.E3N.admin.catalogo.domain.genre.GenreGateway;
import com.E3N.admin.catalogo.domain.genre.GenreId;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase{

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(GenreGateway genreGateway) {
        this.genreGateway = genreGateway;
    }

    @Override
    public GenreOutput execute(String id) {
        final var genreId = GenreId.from(id);
        final var output = genreGateway.findById(genreId)
                .orElseThrow(() -> NotFoundException.with(Genre.class, genreId));
        return GenreOutput.from(output);
    }
}
