package e3n.com.admin.catalogo.domain.genre;

import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
