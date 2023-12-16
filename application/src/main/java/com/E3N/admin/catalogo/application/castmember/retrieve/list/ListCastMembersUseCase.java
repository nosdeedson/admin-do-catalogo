package com.E3N.admin.catalogo.application.castmember.retrieve.list;

import com.E3N.admin.catalogo.application.UseCase;
import e3n.com.admin.catalogo.domain.pagination.Pagination;
import e3n.com.admin.catalogo.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase extends UseCase<SearchQuery, Pagination<CastMemberListOutput>> permits DefaultListCastMembersUseCase{
}
