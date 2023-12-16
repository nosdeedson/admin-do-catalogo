package e3n.com.admin.catalogo.application.castmember.retrieve.list;

import com.E3N.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput;
import com.E3N.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.castmember.CastMemberType;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberJpaEntity;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

@IntegrationTest
public class ListCastMemberUseCaseIT {

    @Autowired
    private ListCastMembersUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        final var members = List.of(
                CastMember.newMember("Eva Mendes", CastMemberType.ACTRESS),
                CastMember.newMember("Mariana Rios", CastMemberType.ACTRESS)
        );

        repository.saveAllAndFlush(members.stream().map(CastMemberJpaEntity::from).toList());

        Assertions.assertEquals(2, repository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 2;
        final var expectedTotal = 2;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var expectedItems = members.stream().map(CastMemberListOutput::from).toList();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var results = useCase.execute(query);

        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedTotal, results.items().size());
        Assertions.assertTrue(expectedItems.size() == results.items().size() && expectedItems.containsAll(results.items()));

        Mockito.verify(gateway, Mockito.times(1)).findAll(Mockito.any());
    }
    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
        Assertions.assertEquals(0, repository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 2;
        final var expectedTotal = 0;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var expectedItems = List.<CastMemberListOutput>of();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var results = useCase.execute(query);

        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedTotal, results.items().size());
        Assertions.assertTrue(expectedItems.size() == results.items().size() && expectedItems.containsAll(results.items()));

        Mockito.verify(gateway, Mockito.times(1)).findAll(Mockito.any());

    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {
        final var members = List.of(
                CastMember.newMember("Eva Mendes", CastMemberType.ACTRESS),
                CastMember.newMember("Mariana Rios", CastMemberType.ACTRESS)
        );

        repository.saveAllAndFlush(members.stream().map(CastMemberJpaEntity::from).toList());

        Assertions.assertEquals(2, repository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 2;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var expectedErrorMessage = "Gateway error";
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(gateway).findAll(Mockito.any());

        final var exception = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));
        Assertions.assertNotNull(exception);

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        // times 1 is default
        Mockito.verify(gateway).findAll(Mockito.any());
    }

}
