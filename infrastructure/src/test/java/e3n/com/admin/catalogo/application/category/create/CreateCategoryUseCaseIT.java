package e3n.com.admin.catalogo.application.category.create;


import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;


@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        Assertions.assertEquals(0, categoryRepository.count());

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var outPut = createCategoryUseCase.execute(command).get();

        Assertions.assertNotNull(outPut);
        Assertions.assertNotNull(outPut.id());

    }
}
