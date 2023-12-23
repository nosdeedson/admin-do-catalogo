package e3n.com.admin.catalogo.infrastructure.configuration;

import e3n.com.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties;
import e3n.com.admin.catalogo.infrastructure.services.StorageService;
import e3n.com.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.catalogo-videos")
    public StorageProperties storageProperties(){
        return new StorageProperties();
    }

    @Bean
    @Profile({"development", "test-integration"})
    public StorageService storageService(){
        return new InMemoryStorageService();
    }
}
