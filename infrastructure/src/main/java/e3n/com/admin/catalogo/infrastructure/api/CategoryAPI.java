package e3n.com.admin.catalogo.infrastructure.api;


import e3n.com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create new category")
    @ApiResponses(
            @ApiResponse(responseCode = "201", description = "Category successfuly created"),
            @ApiResponse(responseCode = "422", description = "Validation error was throw"),
            @ApiResponse(responseCode = "500")
    )
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input);
}
