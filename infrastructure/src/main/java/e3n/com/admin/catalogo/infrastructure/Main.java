package e3n.com.admin.catalogo.infrastructure;

import e3n.com.admin.catalogo.application.UseCase;
import e3n.com.admin.catalogo.domain.category.Category;
import e3n.com.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.servlet.view.tiles3.SpringWildcardServletTilesApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("teste");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "test");
        SpringApplication.run(WebServerConfig.class, args);
    }
}