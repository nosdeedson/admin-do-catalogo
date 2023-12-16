package com.E3N.admin.catalogo.application.castmember.update;

import com.E3N.admin.catalogo.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput> permits DefaultUpdateCastMemberUseCase {
}
