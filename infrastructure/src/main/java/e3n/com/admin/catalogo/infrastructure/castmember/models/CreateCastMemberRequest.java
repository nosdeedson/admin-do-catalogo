package e3n.com.admin.catalogo.infrastructure.castmember.models;

import e3n.com.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}
