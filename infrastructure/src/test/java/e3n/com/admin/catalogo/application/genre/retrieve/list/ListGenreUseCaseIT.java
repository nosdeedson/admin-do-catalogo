package e3n.com.admin.catalogo.application.genre.retrieve.list;

import com.E3N.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.E3N.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class ListGenreUseCaseIT {

    @Autowired
    private ListGenreUseCase useCase;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreGateway genreGateway;

    @Test
    public void givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {
        final var genres = List.of(Genre.newGenre("Action", true), Genre.newGenre("Adventure", true));

        genreRepository.saveAllAndFlush(genres.stream().map(GenreJpaEntity::from).toList());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream().map(GenreListOutput::from).toList();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var results = useCase.execute(query);
        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertTrue(
                expectedItems.size() == results.items().size()
                && expectedItems.containsAll(results.items())
        );
    }

    @Test
    public void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<GenreListOutput>of();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var results = useCase.execute(query);
        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertTrue(
                expectedItems.size() == results.items().size()
                        && expectedItems.containsAll(results.items())
        );
    }
}
