package e3n.com.admin.catalogo.domain.genre;

import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenreTest{

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre(){
        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre(expectedName, expectedIsactive);

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedName, genre.getName());
        Assertions.assertEquals(expectedIsactive, genre.isActive());
        Assertions.assertEquals(expectedCategories, genre.getCategories().size());
        Assertions.assertNotNull(genre.getCreatedAt());
        Assertions.assertNotNull(genre.getUpdatedAt());
        Assertions.assertNull(genre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError(){
        final String expectedName = null;
        final var expectedIsactive = true;
        final var expectedCountError = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var notification = Assertions.assertThrows(NotificationException.class, () ->{
            Genre.newGenre(expectedName, expectedIsactive);
        });
        Assertions.assertEquals(expectedCountError, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAError(){
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final  var expectedErrorMessage = "'name' should not be empty";
        final var notification = Assertions.assertThrows(NotificationException.class, () ->{
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final var expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 caracteres";
        final var notification = Assertions.assertThrows(NotificationException.class, () ->{
           Genre.newGenre(expectedName, expectedIsActive);
        });
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    }

    @Test
    public void givenAnActiveGenre_whenCallDeactivate_shouldReceiveOK(){
        final var expectedName = "Ação";
        final var expectedIsactive = false;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre(expectedName, true);
        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.getDeletedAt());

        final var actualGenre = genre.deactivate();
        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsactive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAnInactiveGenre_whenCallActivate_shouldReceiveOK(){
        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre(expectedName, false);
        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertFalse(genre.isActive());
        Assertions.assertNotNull(genre.getDeletedAt());

        final var currentGenre = genre.activate();

        Assertions.assertNotNull(currentGenre);
        Assertions.assertNotNull(currentGenre.getId());
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsactive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories().size());
        Assertions.assertNotNull(currentGenre.getCreatedAt());
        Assertions.assertNotNull(currentGenre.getUpdatedAt());
        Assertions.assertNull(currentGenre.getDeletedAt());

    }

    @Test
    public void givenAValidInactiveGenre_whenCallUpdateWithActivate_shouldReceiveGenreUpdated(){
        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 1;

        final var genre = Genre.newGenre("action", false);
        final var updated = genre.getUpdatedAt();
        final var actualGenre = genre.update(expectedName, expectedIsactive, List.of(CategoryID.from("123")));

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsactive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedIsactive = false;
        final var expectedCategories = 1;

        final var genre = Genre.newGenre("action", true);
        final var updated = genre.getUpdatedAt();
        Assertions.assertEquals("action", genre.getName());
        final var actualGenre = genre.update(expectedName, expectedIsactive, List.of(CategoryID.from("123")));

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsactive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException(){
        final String expectedName = " ";
        final var expectedIsactive = true;
        final var categories = List.of(CategoryID.from("123"));
        final var expectedCountError = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var genre = Genre.newGenre("Ação", expectedIsactive);

        final var notification = Assertions.assertThrows(NotificationException.class, () ->{
            genre.update(expectedName, expectedIsactive,categories);
        });

        Assertions.assertEquals(expectedCountError, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullName_shouldReceiveNotificationException(){
        final String expectedName = null;
        final var expectedIsactive = true;
        final var categories = List.of(CategoryID.from("123"));
        final var expectedCountError = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var genre = Genre.newGenre("Ação", expectedIsactive);

        final var notification = Assertions.assertThrows(NotificationException.class, () ->{
            genre.update(expectedName, expectedIsactive,categories);
        });

        Assertions.assertEquals(expectedCountError, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK(){
        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre("teste", false);

        Assertions.assertNotNull(genre);
        Assertions.assertEquals(expectedCategories, genre.getCategories().size());
        final var updated = genre.getUpdatedAt();
        final var actualGenre = genre.update(expectedName, expectedIsactive, null);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsactive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOK(){
        CategoryID filme = CategoryID.from("123");
        CategoryID movies = CategoryID.from("456");
        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 2;

        final var genre = Genre.newGenre(expectedName, expectedIsactive);
        Assertions.assertNotNull(genre);
        Assertions.assertEquals(0, genre.getCategories().size());
        Assertions.assertEquals("Ação", genre.getName());

        final var updated = genre.getUpdatedAt();
        final var actualGenre = genre.update(expectedName, expectedIsactive, List.of(filme, movies));

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(actualGenre.getId(), genre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsactive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidNullAsCategoryID_whenCallAddCategory_shouldReceiveOK(){

        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre(expectedName, expectedIsactive);
        Assertions.assertNotNull(genre);
        Assertions.assertEquals(0, genre.getCategories().size());
        Assertions.assertEquals("Ação", genre.getName());

        final var updated = genre.getUpdatedAt();
        final var actualGenre = genre.update(expectedName, expectedIsactive, null);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(actualGenre.getId(), genre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsactive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOK(){
        CategoryID filme = CategoryID.from("123");
        CategoryID movies = CategoryID.from("456");
        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 1;

        final var genre = Genre.newGenre("teste", false);

        Assertions.assertNotNull(genre);
        final var updated = genre.getUpdatedAt();
        final var currentGenre = genre.update(expectedName, expectedIsactive, List.of(filme, movies));

        Assertions.assertNotNull(currentGenre);
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsactive, currentGenre.isActive());
        Assertions.assertEquals(2, currentGenre.getCategories().size());

        final var genreAfterRemoving = currentGenre.removeCategory(filme);
        Assertions.assertNotNull(genreAfterRemoving);
        Assertions.assertEquals(genreAfterRemoving.getId(), genre.getId());
        Assertions.assertEquals(expectedName, genreAfterRemoving.getName());
        Assertions.assertEquals(expectedIsactive, genreAfterRemoving.isActive());
        Assertions.assertEquals(expectedCategories, genreAfterRemoving.getCategories().size());
        Assertions.assertNotNull(genreAfterRemoving.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genreAfterRemoving.getUpdatedAt()));
        Assertions.assertNull(genreAfterRemoving.getDeletedAt());
        Assertions.assertEquals(genreAfterRemoving.getCategories().get(0), movies);
    }

    @Test
    public void givenAnInvalidNullAsCategoryID_whenCallRemoveCategory_shouldReceiveOK(){
        CategoryID filme = CategoryID.from("123");
        CategoryID movies = CategoryID.from("456");
        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 2;

        final var genre = Genre.newGenre("teste", false);

        Assertions.assertNotNull(genre);
        final var updated = genre.getUpdatedAt();
        final var currentGenre = genre.update(expectedName, expectedIsactive, List.of(filme, movies));

        Assertions.assertNotNull(currentGenre);
        Assertions.assertEquals(expectedName, currentGenre.getName());
        Assertions.assertEquals(expectedIsactive, currentGenre.isActive());
        Assertions.assertEquals(expectedCategories, currentGenre.getCategories().size());

        final var genreAfterRemoving = currentGenre.removeCategory(null);
        Assertions.assertNotNull(genreAfterRemoving);
        Assertions.assertEquals(genreAfterRemoving.getId(), genre.getId());
        Assertions.assertEquals(expectedName, genreAfterRemoving.getName());
        Assertions.assertEquals(expectedIsactive, genreAfterRemoving.isActive());
        Assertions.assertEquals(expectedCategories, genreAfterRemoving.getCategories().size());
        Assertions.assertNotNull(genreAfterRemoving.getCreatedAt());
        Assertions.assertTrue(updated.isBefore(genreAfterRemoving.getUpdatedAt()));
        Assertions.assertNull(genreAfterRemoving.getDeletedAt());

    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithEmptyList_shouldReceiveOK(){
        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre(expectedName, expectedIsactive);

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedCategories, genre.getCategories().size());

        final var actualGenre = genre.addCategories(new ArrayList<>());

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsactive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithNullList_shouldReceiveOK(){
        final var expectedName = "Ação";
        final var expectedIsactive = true;
        final var expectedCategories = 0;

        final var genre = Genre.newGenre(expectedName, expectedIsactive);

        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertEquals(expectedCategories, genre.getCategories().size());

        final var actualGenre = genre.addCategories(null);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsactive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }
}
