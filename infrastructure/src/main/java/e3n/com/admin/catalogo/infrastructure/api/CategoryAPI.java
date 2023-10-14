package e3n.com.admin.catalogo.infrastructure.api;


import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import e3n.com.admin.catalogo.infrastructure.category.models.CategoryResponse;
import e3n.com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import e3n.com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create new category")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Category successfuly created"),
            @ApiResponse(responseCode = "422", description = "Validation error was throw"),
            @ApiResponse(responseCode = "500")
        }
    )
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input);

    @GetMapping
    @Operation(summary = "list all categories paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfuly"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "Interanl server error")
    })
    Pagination<CategoryListResponse> listaCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String dir
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfuly"),
            @ApiResponse(responseCode = "404", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server was throw")
    })
    CategoryResponse getById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfuly"),
            @ApiResponse(responseCode = "404", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") final String id, @RequestBody UpdateCategoryRequest input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfuly"),
            @ApiResponse(responseCode = "404", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    void deleteById(@PathVariable(name = "id") final String id);

}
