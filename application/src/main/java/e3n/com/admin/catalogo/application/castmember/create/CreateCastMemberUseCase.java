package e3n.com.admin.catalogo.application.castmember.create;

import e3n.com.admin.catalogo.application.UseCase;

public sealed abstract class CreateCastMemberUseCase extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
