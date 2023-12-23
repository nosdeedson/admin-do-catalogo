package e3n.com.admin.catalogo.infrastructure.services.local;

import e3n.com.admin.catalogo.infrastructure.configuration.json.Json;
import e3n.com.admin.catalogo.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventService implements EventService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InMemoryEventService.class);

    @Override
    public void send(Object event) {
        LOGGER.info("Event was observed: {}", Json.writeValueAsString(event));
    }
}
