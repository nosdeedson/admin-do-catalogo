package com.E3N.admin.catalogo.application.castmember.retrieve.get;

import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;

import java.util.Objects;

public non-sealed class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase{

    private final CastMemberGateway gateway;

    public DefaultGetCastMemberByIdUseCase(CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CastMemberOutput execute(String id) {
        return CastMemberOutput.from(gateway.findById(CastMemberID.from(id))
                .orElseThrow(() -> NotFoundException.with(CastMember.class, CastMemberID.from(id))));
    }
}
