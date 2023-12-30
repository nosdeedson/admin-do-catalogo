package e3n.com.admin.catalogo.application.genre.delete;

import e3n.com.admin.catalogo.IntegrationTest;
import com.E3N.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.E3N.admin.catalogo.domain.genre.Genre;
import com.E3N.admin.catalogo.domain.genre.GenreGateway;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreGateway genreGateway;

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        final var genre = genreGateway.create(Genre.newGenre("Action", true));
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(genre.getId().getValue()));
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var genre = genreGateway.create(Genre.newGenre("Action", true));
        Assertions.assertEquals(1, genreRepository.count());
        final var toTryDelete = GenreId.from("123");

        Assertions.assertDoesNotThrow(() -> useCase.execute(toTryDelete.getValue()));
        Assertions.assertEquals(1, genreRepository.count());
    }
}
