package com.E3N.admin.catalogo.domain;

import com.E3N.admin.catalogo.domain.events.DomainEvent;

import java.util.List;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        super(id);
    }

    protected AggregateRoot(final ID id, List<DomainEvent> events){
        super(id, events);
    }
}
