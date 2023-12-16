package com.E3N.admin.catalogo.application.video.retrieve.list;

import com.E3N.admin.catalogo.application.Fixture;
import com.E3N.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

public class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListVideosUseCase useCase;

    @Mock
    private VideoGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListVideos_shouldReturnVideos() {
        final var videos = List.of(
                new VideoPreview(Fixture.video()),
                new VideoPreview(Fixture.video())
        );

        final var expectedPage = 0;
        final var expectedPerpage = 10;
        final var expectedTerms = "a";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = videos.stream()
                .map(VideoListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerpage, expectedTotal,
                videos
        );

        final var query = new VideoSearchQuery(expectedPage, expectedPerpage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());
        Mockito.when(gateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        final var result = useCase.execute(query);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerpage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());
        Assertions.assertEquals(expectedItems.size(), result.items().size());
        Assertions.assertEquals(expectedItems, result.items());

        Mockito.verify(gateway, Mockito.times(1)).findAll(Mockito.eq(query));
    }

    @Test
    public void givenAValidQuery_whenCallsListVideosAndResultIsEmpty_shouldReturnGenres() {
        final var videos = List.<VideoPreview>of();

        final var expectedPage = 0;
        final var expectedPerpage = 10;
        final var expectedTerms = "a";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedPagination = new Pagination<>(
                expectedPage, expectedPerpage, expectedTotal,
                videos
        );

        final var query = new VideoSearchQuery(expectedPage, expectedPerpage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());

        Mockito.when(gateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerpage, result.perPage());
        Assertions.assertEquals(expectedTotal, result.total());

        Mockito.verify(gateway, Mockito.times(1)).findAll(Mockito.eq(query));

    }

    @Test
    public void givenAValidQuery_whenCallsListVideosAndGatewayThrowsRandomError_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerpage = 10;
        final var expectedTerms = "a";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "An internal server has hapened";

        final var query = new VideoSearchQuery(expectedPage, expectedPerpage, expectedTerms, expectedSort,
                expectedDirection, Set.of(), Set.of(), Set.of());

        Mockito.when(gateway.findAll(query))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        Mockito.verify(gateway, Mockito.times(1)).findAll(Mockito.eq(query));
    }


}
