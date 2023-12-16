package e3n.com.admin.catalogo.infrastructure.castmember.models;

import com.E3N.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(
        String name,
        CastMemberType type
) {
}
