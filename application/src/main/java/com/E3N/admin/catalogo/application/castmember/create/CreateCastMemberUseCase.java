package com.E3N.admin.catalogo.application.castmember.create;

import com.E3N.admin.catalogo.application.UseCase;

public sealed abstract class CreateCastMemberUseCase extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
