package e3n.com.admin.catalogo.infrastructure.genre;


import e3n.com.admin.catalogo.MYSQLGatewayTest;
import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.genre.Genre;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@MYSQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GenreMySQLGateway genreMySQLGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjections(){
        Assertions.assertNotNull(categoryMySQLGateway);
        Assertions.assertNotNull(genreMySQLGateway);
        Assertions.assertNotNull(genreRepository);
    }

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre() throws InterruptedException {
        final var filmes = categoryRepository.saveAndFlush(CategoryJpaEntity.from(Category.newCategory("Filmes", null, true)));
        Assertions.assertEquals(1, categoryRepository.count());
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from(filmes.getId()));

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        genre.addCategories(expectedCategories);

        final var expectedId = genre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        final var updated = genre.getUpdatedAt();
        Thread.sleep(5000);
        final var actualGenre = genreMySQLGateway.create(genre);
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
        // TODO
        //Assertions.assertTrue(updated.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedGenre.getId());
        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIds());
        Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
        // TODO
        // Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdateAt()));
        Assertions.assertNull(persistedGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre(){
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = genre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        final var actual = genreMySQLGateway.create(genre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actual.getId());
        Assertions.assertEquals(expectedName, actual.getName());
        Assertions.assertEquals(expectedIsActive, actual.isActive());
        Assertions.assertEquals(expectedCategories, actual.getCategories());
        Assertions.assertEquals(genre.getCreatedAt(), actual.getCreatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), actual.getDeletedAt());
        // TODO
        //Assertions.assertTrue(updated.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actual.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedGenre.getId());
        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIds());
        Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
        // TODO
        // Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdateAt()));
        Assertions.assertNull(persistedGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre(){
        final var filmes = categoryMySQLGateway.create(Category.newCategory("filmes", null, true));
        final var series = categoryMySQLGateway.create(Category.newCategory("series", null, true));

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var genre = Genre.newGenre("ac", expectedIsActive);

        final var expectedId = genre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals("ac", genre.getName());
        Assertions.assertEquals(0, genre.getCategories().size());

        final var actualGenre = genreMySQLGateway.update(Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedId.getValue(), actualGenre.getId().getValue());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(sorted(actualGenre.getCategories()), sorted(expectedCategories));
//        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        Assertions.assertEquals(persistedGenre.getId(), actualGenre.getId().getValue());
        Assertions.assertEquals(persistedGenre.getName(), actualGenre.getName());
        Assertions.assertEquals(persistedGenre.isActive(), actualGenre.isActive());
        Assertions.assertEquals(persistedGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
        Assertions.assertEquals(sorted(persistedGenre.getCategoryIds()), sorted(expectedCategories));

    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre(){
        final var filmes = categoryMySQLGateway.create(Category.newCategory("filmes", null, true));
        final var series = categoryMySQLGateway.create(Category.newCategory("series", null, true));

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        genre.update(expectedName, expectedIsActive, List.of(filmes.getId(), series.getId()));

        final var expectedId = genre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(2, genre.getCategories().size());

        final var actualGenre = genreMySQLGateway.update(Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories ));
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(sorted(actualGenre.getCategories()), expectedCategories);
//        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        Assertions.assertEquals(persistedGenre.getId(), actualGenre.getId().getValue());
        Assertions.assertEquals(persistedGenre.getName(), actualGenre.getName());
        Assertions.assertEquals(persistedGenre.isActive(), actualGenre.isActive());
        Assertions.assertEquals(persistedGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
        Assertions.assertEquals(sorted(persistedGenre.getCategoryIds()), expectedCategories);

    }

    @Test
    public void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre(){
        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre("test", false);
        final var expectedId = genre.getId();
        Assertions.assertEquals(0, genreRepository.count());
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertFalse(genre.isActive());
        Assertions.assertEquals("test", genre.getName());
        Assertions.assertNotNull(genre.getDeletedAt());

        final var actual = genreMySQLGateway.update(Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedName, actual.getName());
        Assertions.assertEquals(expectedIsActive, actual.isActive());
        Assertions.assertEquals(genre.getCreatedAt(), actual.getCreatedAt());
        Assertions.assertEquals(sorted(actual.getCategories()), expectedCategories);
//        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actual.getUpdatedAt()));
        Assertions.assertNull(actual.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        Assertions.assertEquals(persistedGenre.getId(), expectedId.getValue());
        Assertions.assertEquals(persistedGenre.getName(), expectedName);
        Assertions.assertEquals(persistedGenre.isActive(), expectedIsActive);
        Assertions.assertEquals(persistedGenre.getCreatedAt(), actual.getCreatedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
        Assertions.assertEquals(sorted(persistedGenre.getCategoryIds()), expectedCategories);
    }

    @Test
    public void givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre(){
        final var expectedName = "action";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre("test", true);
        final var expectedId = genre.getId();
        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertTrue(genre.isActive());
        Assertions.assertEquals("test", genre.getName());
        Assertions.assertNull(genre.getDeletedAt());

        final var actual = genreMySQLGateway.update(Genre.with(genre).update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedName, actual.getName());
        Assertions.assertEquals(expectedIsActive, actual.isActive());
        Assertions.assertEquals(genre.getCreatedAt(), actual.getCreatedAt());
        Assertions.assertEquals(sorted(actual.getCategories()), expectedCategories);
//        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actual.getUpdatedAt()));
        Assertions.assertNotNull(actual.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();
        Assertions.assertEquals(persistedGenre.getId(), expectedId.getValue());
        Assertions.assertEquals(persistedGenre.getName(), expectedName);
        Assertions.assertEquals(persistedGenre.isActive(), expectedIsActive);
        Assertions.assertEquals(persistedGenre.getCreatedAt(), actual.getCreatedAt());
        Assertions.assertNotNull(persistedGenre.getDeletedAt());
        Assertions.assertEquals(sorted(persistedGenre.getCategoryIds()), expectedCategories);
    }

    @Test
    public void givenaGenresPersisted_whenCallsExistsByIds_shouldReturnPersistedID(){
        final var genre = Genre.newGenre("Action", true);
        final var expectedId = genre.getId();
        final var itemsPersisted = 1;
        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals(1, genreRepository.count());

        final var genresInDB = genreMySQLGateway.existsByIds(List.of(genre.getId()));

        Assertions.assertEquals(itemsPersisted, genresInDB.size());
        Assertions.assertEquals(expectedId.getValue(), genresInDB.get(0).getValue());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {
        final var genre = Genre.newGenre("Action", true);
        final var expectedId = genre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertDoesNotThrow(() -> genreMySQLGateway.deleteById(expectedId));
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOK() {
        Assertions.assertEquals(0, genreRepository.count());
        Assertions.assertDoesNotThrow(() -> genreMySQLGateway.deleteById(GenreId.from("123")));
        Assertions.assertEquals(0, genreRepository.count());

    }

    @Test
    public void givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre() {
        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        genre.addCategories(expectedCategories);
        final var expectedId = genre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals(1, genreRepository.count());

        final var genreFromDB = genreMySQLGateway.findById(expectedId).get();

        Assertions.assertEquals(genreFromDB.getId().getValue(), expectedId.getValue());
        Assertions.assertEquals(genreFromDB.getName(), expectedName);
        Assertions.assertEquals(genreFromDB.isActive(), expectedIsActive);
        Assertions.assertNotNull(genreFromDB.getCreatedAt());
        Assertions.assertNotNull(genreFromDB.getUpdatedAt());
        Assertions.assertNull(genreFromDB.getDeletedAt());
        Assertions.assertEquals(sorted(genreFromDB.getCategories()), expectedCategories);

    }

    @Test
    public void givenAInvalidGenreId_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, genreRepository.count());

        final var fromDB = genreMySQLGateway.findById(GenreId.from("123"));
        Assertions.assertTrue(fromDB.isEmpty());
    }

    @Test
    public void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
        Assertions.assertEquals(0, genreRepository.count());
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItems = 0;

        final var query =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var allGenres = genreMySQLGateway.findAll(query);
        Assertions.assertEquals(expectedPage, allGenres.currentPage());
        Assertions.assertEquals(expectedPerPage, allGenres.perPage());
        Assertions.assertEquals(expectedItems, allGenres.items().size());
        Assertions.assertEquals(expectedTotal, allGenres.total());
    }

    @ParameterizedTest
    @CsvSource({
            "aç,0,10,1,1,Ação",
            "dr,0,10,1,1,Drama",
            "com,0,10,1,1,Comédia romântica",
            "cien,0,10,1,1,Ficção científica",
            "terr,0,10,1,1,Terror",
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        mockGenres();
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var genres = genreMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, genres.currentPage());
        Assertions.assertEquals(expectedItemsCount, genres.items().size());
        Assertions.assertEquals(expectedTotal, genres.total());
        Assertions.assertEquals(expectedGenreName, genres.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Ação",
            "name,desc,0,10,5,5,Terror",
            "createdAt,asc,0,10,5,5,Comédia romântica",
            "createdAt,desc,0,10,5,5,Ficção científica",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        mockGenres();
        final var expectedTerms = "";
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var genres = genreMySQLGateway.findAll(query);
        Assertions.assertEquals(expectedPage, genres.currentPage());
        Assertions.assertEquals(expectedItemsCount, genres.items().size());
        Assertions.assertEquals(expectedTotal, genres.total());
        Assertions.assertEquals(expectedGenreName, genres.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Ação;Comédia romântica",
            "1,2,2,5,Drama;Ficção científica",
            "2,2,1,5,Terror",
    })
    public void givenAValidPaging_whenCallsFindAll_shouldReturnPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenres
    ) {
        mockGenres();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var genres = genreMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, genres.currentPage());
        Assertions.assertEquals(expectedItemsCount, genres.items().size());
        Assertions.assertEquals(expectedTotal, genres.total());

        int index = 0;
        for (final var expectedName: expectedGenres.split(";")){
            final var name = genres.items().get(index).getName();
            index++;
            Assertions.assertEquals(expectedName, name);
        }
    }

    private List<CategoryID> sorted(final List<CategoryID> ids){
        return ids.stream()
                .sorted(Comparator.comparing(CategoryID::getValue))
                .toList();
    }

    private void mockGenres(){
        genreRepository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Comédia romântica", true)),
                GenreJpaEntity.from(Genre.newGenre("Ação", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true)),
                GenreJpaEntity.from(Genre.newGenre("Ficção científica", true))
        ));
    }
}
