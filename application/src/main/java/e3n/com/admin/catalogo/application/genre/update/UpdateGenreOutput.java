package e3n.com.admin.catalogo.application.genre.update;

import e3n.com.admin.catalogo.domain.genre.Genre;

public record UpdateGenreOutput(String id) {
    public static UpdateGenreOutput from(Genre genre){
        return new UpdateGenreOutput(genre.getId().getValue());
    }
}
