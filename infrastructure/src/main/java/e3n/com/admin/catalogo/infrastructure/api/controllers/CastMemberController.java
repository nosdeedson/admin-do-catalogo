package e3n.com.admin.catalogo.infrastructure.api.controllers;

import com.E3N.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.E3N.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.E3N.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.E3N.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.E3N.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.E3N.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import com.E3N.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.api.CastMemberAPI;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import e3n.com.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest;
import e3n.com.admin.catalogo.infrastructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class CastMemberController implements CastMemberAPI {

    private CreateCastMemberUseCase createCastMemberUseCase;
    private DeleteCastMemberUseCase deleteCastMemberUseCase;
    private GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private ListCastMembersUseCase listCastMembersUseCase;
    private UpdateCastMemberUseCase updateCastMemberUseCase;

    public CastMemberController(
            CreateCastMemberUseCase createCastMemberUseCase, //
            DeleteCastMemberUseCase deleteCastMemberUseCase, //
            GetCastMemberByIdUseCase getCastMemberByIdUseCase, //
            ListCastMembersUseCase listCastMembersUseCase, //
            UpdateCastMemberUseCase updateCastMemberUseCase //
            ) {
        this.createCastMemberUseCase = createCastMemberUseCase;
        this.deleteCastMemberUseCase = deleteCastMemberUseCase;
        this.getCastMemberByIdUseCase = getCastMemberByIdUseCase;
        this.listCastMembersUseCase = listCastMembersUseCase;
        this.updateCastMemberUseCase = updateCastMemberUseCase;
    }

    @Override
    public ResponseEntity<?> create(CreateCastMemberRequest input) {
        final var command = CreateCastMemberCommand.with(input.name(), input.type());
        final var output = createCastMemberUseCase.execute(command);
        return ResponseEntity.created(URI.create("/cast_members/"+output.id())).body(output);
    }

    @Override
    public void deleteById(String id) {
        deleteCastMemberUseCase.execute(id);
    }

    @Override
    public CastMemberResponse getById(String id) {
        final var output = getCastMemberByIdUseCase.execute(id);
        return CastMemberPresenter.present(output);
    }

    @Override
    public Pagination<CastMemberListResponse> list(String search, int page, int perPage, String sort, String direction) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        final var results = listCastMembersUseCase.execute(query);
        return results.map(CastMemberPresenter::present);
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateCastMemberRequest input) {
        final var command = UpdateCastMemberCommand.with(id, input.name(), input.type());
        final var ouput = updateCastMemberUseCase.execute(command);
        return ResponseEntity.ok(ouput);
    }
}
