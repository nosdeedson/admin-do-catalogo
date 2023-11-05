package e3n.com.admin.catalogo.application.castmember.create.delete;

import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;

import java.util.Objects;

public final class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase{

    private CastMemberGateway gateway;

    public DefaultDeleteCastMemberUseCase(CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(String id) {
        this.gateway.deleteById(CastMemberID.from(id));
    }
}
