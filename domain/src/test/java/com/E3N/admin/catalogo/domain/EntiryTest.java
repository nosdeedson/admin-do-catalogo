package com.E3N.admin.catalogo.domain;

import com.E3N.admin.catalogo.domain.events.DomainEvent;
import com.E3N.admin.catalogo.domain.utils.IdUtils;
import com.E3N.admin.catalogo.domain.utils.InstantUtils;
import com.E3N.admin.catalogo.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EntiryTest {

    @Test
    public void givenNullAsEvents_whenInstantiate_shouldBeOk() {
        final List<DomainEvent> events = null;

        final var entity = new DummyEntity(new DummyId(), events);

        Assertions.assertNotNull(entity.getDomainEvents());
        Assertions.assertTrue(entity.getDomainEvents().isEmpty());
    }

    @Test
    public void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone() {
        final List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyEvent());
        final var entity = new DummyEntity(new DummyId(), events);
        Assertions.assertNotNull(entity.getDomainEvents());
        Assertions.assertEquals(1, entity.getDomainEvents().size());
        Assertions.assertThrows(RuntimeException.class, () ->{
            final var currentEvents = entity.getDomainEvents();
            currentEvents.add(new DummyEvent());
        });
    }

    @Test
    public void givenEmptyDomainEvents_whenCallsRegisterEvent_shouldAddEventToList() {
        final var expectedEvents = 1;
        final var entity = new DummyEntity(new DummyId(), new ArrayList<>());
        entity.registerEvent(new DummyEvent());

        Assertions.assertNotNull(entity.getDomainEvents());
        Assertions.assertEquals(expectedEvents, entity.getDomainEvents().size());

    }

    @Test
    public void givenAFewDomainEvents_whenCallsPublishEvents_shouldCallPublisherAndClearTheList() {
        final var expectedEvents = 0;
        final var expectedSentEvents = 2;
        final var counter = new AtomicInteger(0);
        final var entity = new DummyEntity(new DummyId(), new ArrayList<>());

        entity.registerEvent(new DummyEvent());
        entity.registerEvent(new DummyEvent());

        Assertions.assertEquals(2, entity.getDomainEvents().size());
        entity.publishDomainEvents(event -> {
            counter.incrementAndGet();
        });

        Assertions.assertNotNull(entity.getDomainEvents());
        Assertions.assertEquals(expectedEvents, entity.getDomainEvents().size());
        Assertions.assertEquals(expectedSentEvents, counter.get());
    }

    public static class DummyEvent implements DomainEvent{

        @Override
        public Instant occurredOn() {
            return InstantUtils.now();
        }
    }

    public static class DummyId extends Identifier{

        private final String id;

        public DummyId() {
            this.id = IdUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }

    public static class  DummyEntity extends  Entity<DummyId>{

        public DummyEntity(DummyId id, List<DomainEvent> events){
            super(id, events);
        }

        @Override
        public void validate(ValidationHandler handler) {

        }
    }
}
