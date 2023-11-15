package e3n.com.admin.catalogo.infrastructure.api.controllers;

import e3n.com.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import e3n.com.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import e3n.com.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import e3n.com.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import e3n.com.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import e3n.com.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import e3n.com.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import e3n.com.admin.catalogo.application.castmember.update.UpdateCastMemberOutput;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;
import e3n.com.admin.catalogo.infrastructure.api.CastMemberAPI;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import e3n.com.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest;
import e3n.com.admin.catalogo.infrastructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController("cast")
public class CastMemberController implements CastMemberAPI {

    private DefaultCreateCastMemberUseCase createCastMemberUseCase;
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;
    private DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private DefaultListCastMembersUseCase listCastMembersUseCase;
    private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

    public CastMemberController(
            DefaultCreateCastMemberUseCase createCastMemberUseCase, //
            DefaultDeleteCastMemberUseCase deleteCastMemberUseCase, //
            DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase, //
            DefaultListCastMembersUseCase listCastMembersUseCase, //
            DefaultUpdateCastMemberUseCase updateCastMemberUseCase //
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
