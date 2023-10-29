package e3n.com.admin.catalogo.infrastructure.configuration.usecases;

import e3n.com.admin.catalogo.application.genre.create.CreateGenreUseCase;
import e3n.com.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import e3n.com.admin.catalogo.application.genre.delelte.DefaultDeleteGenreUseCase;
import e3n.com.admin.catalogo.application.genre.delelte.DeleteGenreUseCase;
import e3n.com.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import e3n.com.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import e3n.com.admin.catalogo.application.genre.retrieve.list.DefaultListGenreUseCase;
import e3n.com.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import e3n.com.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import e3n.com.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public GenreUseCaseConfig(CategoryGateway categoryGateway, GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase(){
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase(){
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase(){
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase(){
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase(){
        return new DefaultUpdateGenreUseCase(genreGateway, categoryGateway);
    }
}
