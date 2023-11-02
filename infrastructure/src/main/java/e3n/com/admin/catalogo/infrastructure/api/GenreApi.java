package e3n.com.admin.catalogo.infrastructure.api;

import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import e3n.com.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import e3n.com.admin.catalogo.infrastructure.genre.models.GenreResponse;
import e3n.com.admin.catalogo.infrastructure.genre.models.UpdatedGenreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "genres")
@Tag(name = "Genre")
public interface GenreApi {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created successfuly"),
            @ApiResponse(responseCode = "422", description = "A validation error was thorw"),
            @ApiResponse(responseCode = "500", description = "An internal server error has happened")
    })
    ResponseEntity<?> create(@RequestBody CreateGenreRequest input);

    @GetMapping
    @Operation(summary = "List all genres paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "listed successfuly"),
            @ApiResponse(responseCode = "422", description = "invalid parameter received"),
            @ApiResponse(responseCode = "500", description = "An internal server error has happened")
    })
    Pagination<GenreListResponse> list(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get genre by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "found successfuly"),
            @ApiResponse(responseCode = "404", description = "genre not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error has happened")
    })
    GenreResponse getById(@PathVariable(name = "id") final String id);

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "udpated successfuly"),
            @ApiResponse(responseCode = "404", description = "genre not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error has happened")
    })
    ResponseEntity<?> updateById(@RequestBody UpdatedGenreRequest input, @PathVariable(name = "id")final String id);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete genre by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "deleted successfuly"),
            @ApiResponse(responseCode = "404", description = "genre nor found"),
            @ApiResponse(responseCode = "500", description = "An internal server error has happened")
    })
    void deleteById(@PathVariable(name = "id") final String id);

}
