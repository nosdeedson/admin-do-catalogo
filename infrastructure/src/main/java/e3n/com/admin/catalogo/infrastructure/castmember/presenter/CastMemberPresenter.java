package e3n.com.admin.catalogo.infrastructure.castmember.presenter;

import com.E3N.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import com.E3N.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CastMemberListResponse;
import e3n.com.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {

    static CastMemberResponse present(final CastMemberOutput output){
        return new CastMemberResponse(output.id(), output.name(), output.type().name(), output.createdAt().toString(), output.updatedAt().toString());
    }

    static CastMemberListResponse present(final CastMemberListOutput output){
        return new CastMemberListResponse(output.id(), output.name(), output.type().name(), output.createdAt().toString());
    }
}
