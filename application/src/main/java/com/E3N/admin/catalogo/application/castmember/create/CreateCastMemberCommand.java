package com.E3N.admin.catalogo.application.castmember.create;

import com.E3N.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberCommand(
        String name,
        CastMemberType type
) {
    public static CreateCastMemberCommand with(final String name, final CastMemberType type){
        return new CreateCastMemberCommand(name, type);
    }
}
