package com.E3N.admin.catalogo.application.castmember.retrieve.list;

import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Objects;

@SuppressWarnings("all")
public non-sealed class DefaultListCastMembersUseCase extends ListCastMembersUseCase {

    private CastMemberGateway gateway;

    public DefaultListCastMembersUseCase(CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(SearchQuery searchQuery) {
        return gateway.findAll(searchQuery).map(CastMemberListOutput::from);
    }
}
