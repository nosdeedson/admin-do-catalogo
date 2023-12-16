package e3n.com.admin.catalogo.infrastructure.api.controllers;

import com.E3N.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.E3N.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.E3N.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.E3N.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.E3N.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.E3N.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.E3N.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.E3N.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.E3N.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;
import e3n.com.admin.catalogo.infrastructure.api.CategoryAPI;
import e3n.com.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import e3n.com.admin.catalogo.infrastructure.category.models.CategoryResponse;
import e3n.com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import e3n.com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import e3n.com.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryByIdUseCase getCategoryByIdUseCase, UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryRequest input) {
        final var command = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification -> ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/"+ output.id())).body(output);

        return this.createCategoryUseCase.execute(command).fold(onError, onSuccess);

    }

    @Override
    public Pagination<CategoryListResponse> listaCategories(String search, int page, int perPage, String sort, String dir) {
        final var query = new SearchQuery(page, perPage, search, sort, dir);
        return listCategoriesUseCase.execute(query).map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(String id) {
        final var output = getCategoryByIdUseCase.execute(id);
        return CategoryApiPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateCategoryRequest input) {
        final var command = UpdateCategoryCommand.wit(id, input.name(), input.description(), input.isActive());

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return updateCategoryUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public void deleteById(String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}
