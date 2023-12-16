package com.E3N.admin.catalogo.application.castmember.create;

import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

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
