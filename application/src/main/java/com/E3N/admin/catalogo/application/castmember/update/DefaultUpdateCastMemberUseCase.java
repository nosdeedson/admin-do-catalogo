package com.E3N.admin.catalogo.application.castmember.update;

import e3n.com.admin.catalogo.domain.castmember.CastMember;
import e3n.com.admin.catalogo.domain.castmember.CastMemberGateway;
import e3n.com.admin.catalogo.domain.castmember.CastMemberID;
import e3n.com.admin.catalogo.domain.exceptions.NotFoundException;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

public non-sealed class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway gateway;

    public DefaultUpdateCastMemberUseCase(CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public UpdateCastMemberOutput execute(UpdateCastMemberCommand updateCastMemberCommand) {
        final var id = CastMemberID.from(updateCastMemberCommand.id());
        final var member = gateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, id));
        final var notification = Notification.create();
        notification.validate(() -> member.update(updateCastMemberCommand.name(), updateCastMemberCommand.type()));
        if (notification.hasError()) {
            throw new NotificationException("Could not update Aggregate CastMember %s".formatted(id.getValue()), notification);
        }
        return UpdateCastMemberOutput.from(this.gateway.update(member));
    }
}
