package com.E3N.admin.catalogo.domain.castmember;

import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {
    CastMember create(CastMember member);

    void deleteById(CastMemberID id);

    List<CastMemberID> existsByIds(Iterable<CastMemberID> ids);

    Optional<CastMember> findById(CastMemberID id);

    Pagination<CastMember> findAll(SearchQuery query);

    CastMember update(CastMember member);

}
