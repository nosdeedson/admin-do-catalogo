package e3n.com.admin.catalogo.application.castmember.update;

import e3n.com.admin.catalogo.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput> permits DefaultUpdateCastMemberUseCase {
}
