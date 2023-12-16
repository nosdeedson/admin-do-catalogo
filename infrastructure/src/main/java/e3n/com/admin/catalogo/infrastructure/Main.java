package e3n.com.admin.catalogo.infrastructure;

import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import e3n.com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import e3n.com.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("teste");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "test");
        SpringApplication.run(WebServerConfig.class, args);
    }

    //@Bean
    public ApplicationRunner runner(CategoryRepository repository){
        return args -> {
            List<CategoryJpaEntity> all = repository.findAll();
            Category c = Category.newCategory("Filmes", "", true);
            repository.save(CategoryJpaEntity.from(c));
            repository.deleteAll();
        };
    }
}