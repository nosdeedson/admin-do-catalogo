package e3n.com.admin.catalogo.application.category.retrieve.list;

import e3n.com.admin.catalogo.application.UseCaseTest;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListCategoriesUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories(){
        final var categories = List.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Series", null, true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination =
                new Pagination<Category>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        Mockito.when(categoryGateway.findAll(Mockito.eq(aQuery)))
                .thenReturn(expectedPagination);

        final var result = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, result.items().size());
        Assertions.assertEquals(expectedResult, result);
        Assertions.assertEquals(expectedPage, result.currentPage());
        Assertions.assertEquals(expectedPerPage, result.perPage());
        Assertions.assertEquals(categories.size(), result.total());


    }
}
