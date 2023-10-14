package e3n.com.admin.catalogo;


import e3n.com.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("development")
@ComponentScan(
        basePackages = "com.fullcycle.admin.catalogo",
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySqlGateway]")
        }
)
@SpringBootTest(classes = WebServerConfig.class)
@DataJpaTest
@ExtendWith(MysqlCleanUpExtension.class)
public @interface MySQLGatewayTest {
}

