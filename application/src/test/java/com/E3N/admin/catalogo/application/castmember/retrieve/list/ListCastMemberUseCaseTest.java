package com.E3N.admin.catalogo.application.castmember.retrieve.list;

import com.E3N.admin.catalogo.application.Fixture;
import com.E3N.admin.catalogo.application.UseCaseTest;
import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCastMembersUseCase useCase;

    @Mock
    private CastMemberGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        final var members = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
        );
        final var expectedPage = 0;
        final var expectedPerpage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream()
                .map(CastMemberListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerpage,
                expectedTotal,
                members
        );

        Mockito.when(gateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage,expectedPerpage, expectedTerms, expectedSort, expectedDirection);

        final var results = useCase.execute(query);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerpage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItems, results.items());

        Mockito.verify(gateway, Mockito.times(1)).findAll(Mockito.eq(query));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
        final var members = List.<CastMember>of();
        final var expectedPage = 0;
        final var expectedPerpage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = members.stream()
                .map(CastMemberListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerpage,
                expectedTotal,
                members
        );

        Mockito.when(gateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage,expectedPerpage, expectedTerms, expectedSort, expectedDirection);

        final var results = useCase.execute(query);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerpage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItems, results.items());

        Mockito.verify(gateway, Mockito.times(1)).findAll(Mockito.eq(query));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {

        final var expectedPage = 0;
        final var expectedPerpage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        final var query = new SearchQuery(expectedPage, expectedPerpage, expectedTerms, expectedSort, expectedDirection);

        Mockito.when(gateway.findAll(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        Mockito.verify(gateway, Mockito.times(1)).findAll(Mockito.eq(query));

    }

}
