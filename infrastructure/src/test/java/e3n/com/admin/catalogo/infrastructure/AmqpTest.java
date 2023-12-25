package e3n.com.admin.catalogo.infrastructure;

import e3n.com.admin.catalogo.IntegrationTest;
import e3n.com.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles({"test-integration", "development"})
@SpringBootTest(classes = WebServerConfig.class)
@Tag("integrationTest")
public @interface AmqpTest {
}
