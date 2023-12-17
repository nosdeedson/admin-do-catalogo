package com.E3N.admin.catalogo.application.castmember.create;

import com.E3N.admin.catalogo.domain.castmember.CastMember;
import com.E3N.admin.catalogo.domain.castmember.CastMemberGateway;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

@SuppressWarnings("all")
public non-sealed class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase{

    private CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase( final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(CreateCastMemberCommand createCastMemverCommand) {
        final var notification = Notification.create();

        final var member = notification.validate(
                () -> CastMember.newMember(createCastMemverCommand.name(), createCastMemverCommand.type())
        );

        if (notification.hasError()){
            throw new NotificationException("Could not create Aggreagate CastMember", notification);
        }

        return CreateCastMemberOutput.from(castMemberGateway.create(member));
    }
}
