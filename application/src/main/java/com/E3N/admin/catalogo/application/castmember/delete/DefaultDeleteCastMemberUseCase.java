package com.E3N.admin.catalogo.application.castmember.delete;

import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;

import java.util.Objects;

@SuppressWarnings("all")
public non-sealed class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase{

    private CastMemberGateway gateway;

    public DefaultDeleteCastMemberUseCase(CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(String id) {
        this.gateway.deleteById(CastMemberID.from(id));
    }
}
