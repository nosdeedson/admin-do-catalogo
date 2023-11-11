package e3n.com.admin.catalogo.infrastructure.castmember;


import e3n.com.admin.catalogo.MYSQLGatewayTest;
import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.castmember.CastMemberType;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberJpaEntity;
import e3n.com.admin.catalogo.infrastructure.castmember.persitence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MYSQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway gateway;

    @Autowired
    private CastMemberRepository repository;

    @Test
    public void testDependencies(){
        Assertions.assertNotNull(gateway);
        Assertions.assertNotNull(repository);
    }

    @Test
    public void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
        final var expectedName = "Eva mendes";
        final var expectedType = CastMemberType.ACTRESS;

        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        Assertions.assertEquals(0, repository.count());

        final var actualMember = gateway.create(CastMember.with(member));

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(member.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(member.getCreatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = gateway.findById(expectedId).get();
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedId, persistedMember.getId());
        Assertions.assertEquals(member.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(member.getCreatedAt(), persistedMember.getUpdatedAt());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdate_shouldRefreshIt() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;

        final var member = CastMember.newMember("expectedname", CastMemberType.ACTOR);
        final var expectedId = member.getId();

        Assertions.assertEquals(0, repository.count());

        repository.save(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());

        final var currentMember = gateway.update(CastMember.with(member).update(expectedName, expectedType));
        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals(expectedType, currentMember.getType());
        Assertions.assertEquals(expectedName, currentMember.getName());
        Assertions.assertEquals(expectedId, currentMember.getId());
        Assertions.assertEquals(member.getCreatedAt(), currentMember.getCreatedAt());
        Assertions.assertTrue(member.getCreatedAt().isBefore(currentMember.getUpdatedAt()));

        final var persistedMember = gateway.findById(expectedId).get();
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedId, persistedMember.getId());
        Assertions.assertEquals(member.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertTrue(member.getCreatedAt().isBefore(persistedMember.getUpdatedAt()));

    }

    @Test
    public void givenTwoCastMembersAndOnePersisted_whenCallsExistsByIds_shouldReturnPersistedID() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;

        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();
        final var expectedItemsCount = 1;

        Assertions.assertEquals(0, repository.count());

        repository.save(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());

        // TODO ERRO DO ID INCLUINDO ESPAÇO
        final var ids = gateway.existsByIds(List.of(CastMemberID.from("123"), expectedId));

        Assertions.assertEquals(expectedItemsCount, ids.size());
        Assertions.assertEquals(expectedId.getValue(), ids.get(0).getValue());
    }

    @Test
    public void givenAValidCastMember_whenCallsDeleteById_shouldDeleteIt() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;

        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        Assertions.assertEquals(0, repository.count());

        repository.save(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());

        Assertions.assertDoesNotThrow(() -> gateway.deleteById(expectedId));

        Assertions.assertEquals(0, repository.count());

    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeIgnored() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;

        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = CastMemberID.from("123");

        Assertions.assertEquals(0, repository.count());

        repository.save(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());

        Assertions.assertDoesNotThrow(() -> gateway.deleteById(expectedId));

        Assertions.assertEquals(1, repository.count());
    }

    @Test
    public void givenAValidCastMember_whenCallsFindById_shouldReturnIt() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;

        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        Assertions.assertEquals(0, repository.count());

        repository.save(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());

        final var memberFromBD = gateway.findById(expectedId).get();

        Assertions.assertEquals(expectedId, memberFromBD.getId());
        Assertions.assertEquals(expectedName, memberFromBD.getName());
        Assertions.assertEquals(expectedType, memberFromBD.getType());
        Assertions.assertEquals(member.getCreatedAt(), memberFromBD.getCreatedAt());
        Assertions.assertEquals(member.getUpdatedAt(), memberFromBD.getUpdatedAt());

    }

    @Test
    public void givenAnInvalidId_whenCallsFindById_shouldReturnEmpty() {
        final var expectedName = "Eva Mendes";
        final var expectedType = CastMemberType.ACTRESS;

        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = CastMemberID.from("123");

        Assertions.assertEquals(0, repository.count());

        repository.save(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());

        final var memberFromBD = gateway.findById(expectedId);
        Assertions.assertTrue(memberFromBD.isEmpty());
    }

    @Test
    public void givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItemsCount = 0;
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var results = gateway.findAll(query);
        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItemsCount, results.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "taran,0,10,1,1,Quentin Tarantino",
            "jas,0,10,1,1,Jason Momoa",
            "har,0,10,1,1,Kit Harington",
            "MAR,0,10,1,1,Martin Scorsese",
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {

        mockMembers();
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var results = gateway.findAll(query);
        Assertions.assertEquals(expectedPage, results.currentPage() );
        Assertions.assertEquals(expectedPerPage, results.perPage() );
        Assertions.assertEquals(expectedTotal, results.total() );
        Assertions.assertEquals(expectedItemsCount, results.items().size() );
        Assertions.assertEquals(expectedPage, results.currentPage() );
        Assertions.assertEquals(expectedName, results.items().get(0).getName() );

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Jason Momoa",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt,asc,0,10,5,5,Kit Harington",
            "createdAt,desc,0,10,5,5,Martin Scorsese",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        mockMembers();
        final var expectedTerms = "";
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var results = gateway.findAll(query);
        Assertions.assertEquals(expectedItemsCount, results.items().size());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedName, results.items().get(0).getName());
        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Jason Momoa;Kit Harington",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel",
    })
    public void givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedNames
    ) {
        mockMembers();
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var results = gateway.findAll(query);
        Assertions.assertEquals(expectedPage, results.currentPage());
        Assertions.assertEquals(expectedPerPage, results.perPage());
        Assertions.assertEquals(expectedTotal, results.total());
        Assertions.assertEquals(expectedItemsCount, results.items().size());
        int index = 0;
        for (String name : expectedNames.split(";")){
            Assertions.assertEquals(name, results.items().get(index).getName());
            index++;
        }
    }

    private void mockMembers() {
        repository.saveAllAndFlush(List.of(
                CastMemberJpaEntity.from(CastMember.newMember("Kit Harington", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR))
        ));
    }

}
