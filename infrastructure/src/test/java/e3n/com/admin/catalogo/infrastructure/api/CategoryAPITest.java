package e3n.com.admin.catalogo.infrastructure.api;

import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.validation.handler.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import e3n.com.admin.catalogo.ApiTest;
import e3n.com.admin.catalogo.ControllerTest;
import com.E3N.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.E3N.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.E3N.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.E3N.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.E3N.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.E3N.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.E3N.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.E3N.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.E3N.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.E3N.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.DomainException;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import e3n.com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("all")
@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expecterIsActive = true;

        final var input = new CreateCategoryRequest(expectedName, expectedDescription, expecterIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(CreateCategoryOutput.from("123"));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // then

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/categories/123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123")));

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(
                Mockito.argThat(
                        cmd -> Objects.equals(cmd.name(), expectedName)
                                && Objects.equals(cmd.description(), expectedDescription)
                                && Objects.equals(cmd.isActive(), expecterIsActive)
                ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        //given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var input =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedMessage))));

        //when
        final var request = MockMvcRequestBuilders.post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros[0].message", Matchers.equalTo(expectedMessage)));

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(( cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())

        )));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception{
        final var expectedName = "Filmes";
        final var expectedDescription = "a";
        final var expectedIsActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = category.getId().getValue();

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenReturn(CategoryOutput.from(category));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(category.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(category.getUpdatedAt().toString())));

        Mockito.verify(getCategoryByIdUseCase, Mockito.times(1)).execute(expectedId);
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception{
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception{
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "a";
        final var expectedIsActive = true;

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(UpdateCategoryOutput.from(expectedId));

        final var command =
                new UpdateCategoryCommand(expectedId, expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat( cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception{
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "a";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var command =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.erros[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(
                cmd -> Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() throws  Exception{
        final var expectedId = "not-found";
        final var expectedName = "Filmes";
        final var expectedDescription = "a";
        final var expectedIsActive = true;
        final var expetedErrorMesage = "Category with ID not-found was not found";

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var command = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expetedErrorMesage)));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(
                cmd -> Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));

    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldReturnNoContent() throws Exception{
        final var expectedId = "123";

        Mockito.doNothing().when(deleteCategoryUseCase).execute(Mockito.any());

        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteCategoryUseCase, Mockito.times(1))
                .execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception{
        final var aCategory = Category.newCategory("Movies", null, true);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        Mockito.when(listCategoriesUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aCategory.getId().getValue())));

        Mockito.verify(listCategoriesUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedPage, cmd.page())
                        && Objects.equals(expectedPerPage, cmd.perPage())
                        && Objects.equals(expectedDirection, cmd.direction())
                        && Objects.equals(expectedSort, cmd.sort())
                        && Objects.equals(expectedTerms, cmd.terms())
        ));
    }
}
