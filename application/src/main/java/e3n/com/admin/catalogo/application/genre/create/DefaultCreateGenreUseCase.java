package e3n.com.admin.catalogo.application.genre.create;

import e3n.com.admin.catalogo.domain.category.CategoryGateway;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.domain.genre.Genre;
import e3n.com.admin.catalogo.domain.genre.GenreGateway;
import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase{

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultCreateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public CreateGenreOutput execute(CreateGenreCommand createGenreCommand) {

        final var notification = Notification.create();
        notification.append(validateCategories(toCategoryId(createGenreCommand.categories())));
        final var genre = notification.validate(() -> Genre.newGenre(createGenreCommand.name(), createGenreCommand.isActive()));

        if (notification.hasError()){
            throw new NotificationException("Could not create Aggreate Genre", notification);
        }

        genre.addCategories(toCategoryId(createGenreCommand.categories()));
        return CreateGenreOutput.from(this.genreGateway.create(genre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids){
        final var notification = Notification.create();
        if(ids == null || ids.isEmpty()){
            return notification;
        }

        final var retrivedIds = categoryGateway.existByIds(ids);
        if (ids.size() != retrivedIds.size()){
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrivedIds);
            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }
        return notification;
    }

    private List<CategoryID> toCategoryId(final List<String> categories){
        return categories.stream().map(CategoryID::from).toList();
    }
}
