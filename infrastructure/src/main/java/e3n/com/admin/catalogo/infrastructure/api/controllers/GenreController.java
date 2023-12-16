package e3n.com.admin.catalogo.infrastructure.api.controllers;

import com.E3N.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.E3N.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.E3N.admin.catalogo.application.genre.delelte.DeleteGenreUseCase;
import com.E3N.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.E3N.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.E3N.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.E3N.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.api.GenreApi;
import e3n.com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import e3n.com.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import e3n.com.admin.catalogo.infrastructure.genre.models.GenreResponse;
import e3n.com.admin.catalogo.infrastructure.genre.models.UpdatedGenreRequest;
import e3n.com.admin.catalogo.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreApi {

    final private CreateGenreUseCase createGenreUseCase;
    final private DeleteGenreUseCase deleteGenreUseCase;
    final private UpdateGenreUseCase updateGenreUseCase;
    final private ListGenreUseCase listGenreUseCase;
    final private GetGenreByIdUseCase genreByIdUseCase;

    public GenreController(CreateGenreUseCase createGenreUseCase, DeleteGenreUseCase deleteGenreUseCase, UpdateGenreUseCase updateGenreUseCase, ListGenreUseCase listGenreUseCase, GetGenreByIdUseCase genreByIdUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.listGenreUseCase = listGenreUseCase;
        this.genreByIdUseCase = genreByIdUseCase;
    }

    @Override
    public ResponseEntity<?> create(CreateGenreRequest input) {
        final  var createGenre = CreateGenreCommand.with(input.name(), input.isActive(), input.categories());
        final var output = createGenreUseCase.execute(createGenre);
        return ResponseEntity.created(URI.create("/genres/"+output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(String search, int page, int perPage, String sort, String direction) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return listGenreUseCase.execute(query).map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(String id) {
        final var output = genreByIdUseCase.execute(id);
        return GenreApiPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> updateById(UpdatedGenreRequest input, String id) {
        final var command = UpdateGenreCommand.from(id, input.name(), input.isActive(), input.categories());
        final var output = updateGenreUseCase.execute(command);
        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(String id) {
        deleteGenreUseCase.execute(id);
    }
}
