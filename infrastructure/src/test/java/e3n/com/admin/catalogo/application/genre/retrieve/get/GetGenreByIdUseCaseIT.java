package e3n.com.admin.catalogo.application.genre.retrieve.get;

import com.E3N.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import e3n.com.admin.catalogo.IntegrationTest;
import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.genre.Genre;
import com.E3N.admin.catalogo.domain.genre.GenreGateway;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import e3n.com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class GetGenreByIdUseCaseIT {

    @Autowired
    private GetGenreByIdUseCase useCase;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = genreGateway.create(
                Genre.newGenre(expectedName, expectedIsActive)
                        .addCategories(expectedCategories)
        );
        final var expectedId = genre.getId();

        final var output = useCase.execute(expectedId.getValue());

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.id());
        Assertions.assertTrue(expectedCategories.size() == output.categories().size());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedIsActive, output.isActive());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        Assertions.assertNull(output.deletedAt());

    }

    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        final var exception = Assertions.assertThrows(NotFoundException.class, () ->  useCase.execute(GenreId.from("123").getValue()));

        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
