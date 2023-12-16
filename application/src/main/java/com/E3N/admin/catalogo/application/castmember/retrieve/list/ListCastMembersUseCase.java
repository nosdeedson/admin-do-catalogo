package com.E3N.admin.catalogo.application.castmember.retrieve.list;

import com.E3N.admin.catalogo.application.UseCase;
import com.E3N.admin.catalogo.domain.pagination.Pagination;
import com.E3N.admin.catalogo.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase extends UseCase<SearchQuery, Pagination<CastMemberListOutput>> permits DefaultListCastMembersUseCase{
}
