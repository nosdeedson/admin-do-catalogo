package com.E3N.admin.catalogo.domain;

import com.E3N.admin.catalogo.domain.events.DomainEvent;
import com.E3N.admin.catalogo.domain.events.DomainEventPublisher;
import com.E3N.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> {

    protected final ID id;
    private final List<DomainEvent> domainEvents;

    public Entity(ID id) {
        this(id, null);
    }

    protected Entity(ID id, final List<DomainEvent> domainEvents) {
        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
        this.domainEvents = new ArrayList<>(domainEvents == null ?
                Collections.emptyList(): domainEvents);
    }

    public abstract void validate(ValidationHandler handler);

    public void publishDomainEvents(final DomainEventPublisher publisher){
        if (publisher == null){
            return;
        }
        getDomainEvents()
                .forEach(publisher::publishEvent);
        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event){
        if (event == null){
            return;
        }
        this.domainEvents.add(event);
    }

    public ID getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
