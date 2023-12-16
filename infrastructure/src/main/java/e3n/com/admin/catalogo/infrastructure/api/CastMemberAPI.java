package e3n.com.admin.catalogo.infrastructure.api;


import com.E3N.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import e3n.com.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "cast_members")
@Tag(name = "Cast Members")
public interface CastMemberAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a cast member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfuly"),
            @ApiResponse(responseCode = "422", description = "A validation error was throw"),
            @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    ResponseEntity<?> create(@RequestBody CreateCastMemberRequest input);

    @DeleteMapping(value = "{id}")
    @Operation(summary = "Delete a Cast member by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cast member retrieved successfuly"),
            @ApiResponse(responseCode = "500", description = "An internar server was thrown")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable String id);

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a Cast member by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast member retrieved successfuly"),
            @ApiResponse(responseCode = "404", description = "Cast member was not found"),
            @ApiResponse(responseCode = "500", description = "An internar server was thrown")
    })
    CastMemberResponse getById(@PathVariable String id);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List all cast members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast members retrieved"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<CastMemberListResponse> list(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a Cast member by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cast member retrieved successfuly"),
            @ApiResponse(responseCode = "404", description = "Cast member was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was throw"),
            @ApiResponse(responseCode = "500", description = "An internar server was thrown")

    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateCastMemberRequest input);


}
