package com.E3N.admin.catalogo.application.castmember.update;

import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberID;

public record UpdateCastMemberOutput(String id) {

    public static UpdateCastMemberOutput from(final CastMemberID id){
        return new UpdateCastMemberOutput(id.getValue());
    }

    public static UpdateCastMemberOutput from(final CastMember member){
        return new UpdateCastMemberOutput(member.getId().getValue());
    }
}
