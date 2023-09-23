package e3n.com.admin.catalogo.infrastructure;


import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("development")
@DataJpaTest
@ComponentScan(
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*MySqlGateway")
        }
)
@ExtendWith(MySQLGatewayTest.CleanUpExtension.class)
public @interface MySQLGatewayTest {

    class CleanUpExtension implements BeforeEachCallback {

        @Override
        public void beforeEach(final ExtensionContext context) throws Exception {
            final  var repositories = SpringExtension.getApplicationContext(context)
                    .getBeansOfType(CrudRepository.class)
                    .values();
            cleanUp(repositories);
        }

        private void cleanUp(Collection<CrudRepository> repositories){
            repositories.forEach(CrudRepository::deleteAll);
        }
    }
}
