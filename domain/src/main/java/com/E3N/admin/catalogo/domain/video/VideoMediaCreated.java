package com.E3N.admin.catalogo.domain.video;

import com.E3N.admin.catalogo.domain.events.DomainEvent;
import com.E3N.admin.catalogo.domain.utils.InstantUtils;

import java.time.Instant;

public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant ocurredon
) implements DomainEvent {
    public VideoMediaCreated(final String resourceId, final String filePath){
        this(resourceId, filePath, InstantUtils.now());
    }

    @Override
    public Instant occurredOn() {
        return InstantUtils.now();
    }
}
