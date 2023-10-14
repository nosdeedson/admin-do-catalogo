package e3n.com.admin.catalogo;


import e3n.com.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(MysqlCleanUpExtension.class)
@Tag("integrationTest")
public @interface IntegrationTest {
}
