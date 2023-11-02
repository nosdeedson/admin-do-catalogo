package e3n.com.admin.catalogo.application.genre.retrieve.list;

import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListGenre_shouldReturnGenres(){
        final var genres = List.of(
                Genre.newGenre("Action", true),
                Genre.newGenre("Adventure", true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "axc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream()
                .map(GenreListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        Mockito.when(genreGateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var output = useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems, output.items());

        Mockito.verify(genreGateway, Mockito.times(1)).findAll(Mockito.eq(query));
    }

    @Test
    public void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres(){
        final var genres = List.<Genre>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = List.<GenreListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        Mockito.when(genreGateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var output = useCase.execute(query);

        Assertions.assertEquals(expectedPage, output.currentPage());
        Assertions.assertEquals(expectedPerPage, output.perPage());
        Assertions.assertEquals(expectedTotal, output.total());
        Assertions.assertEquals(expectedItems, output.items());

        Mockito.verify(genreGateway, Mockito.times(1)).findAll(Mockito.eq(query));
    }

    @Test
    public void givenAValidQuery_whenCallsListGenreAndGatewayThrowsRandomError_shouldReturnException(){
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";
        Mockito.when(genreGateway.findAll(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var exepection = Assertions.assertThrows(
                IllegalStateException.class, () -> useCase.execute(query)
        );

        Assertions.assertEquals(expectedErrorMessage, exepection.getMessage());
        Mockito.verify(genreGateway, Mockito.times(1)).findAll(Mockito.eq(query));
    }
}
