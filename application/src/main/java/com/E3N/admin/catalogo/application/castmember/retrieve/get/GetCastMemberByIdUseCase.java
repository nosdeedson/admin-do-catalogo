package com.E3N.admin.catalogo.application.castmember.retrieve.get;

import com.E3N.admin.catalogo.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase extends UseCase<String, CastMemberOutput> permits DefaultGetCastMemberByIdUseCase {
}
