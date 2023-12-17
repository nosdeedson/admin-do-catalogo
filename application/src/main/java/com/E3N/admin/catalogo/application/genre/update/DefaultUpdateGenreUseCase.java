package com.E3N.admin.catalogo.application.genre.update;

import com.E3N.admin.catalogo.domain.category.CategoryGateway;
import com.E3N.admin.catalogo.domain.category.CategoryID;
import com.E3N.admin.catalogo.domain.exceptions.NotFoundException;
import com.E3N.admin.catalogo.domain.exceptions.NotificationException;
import com.E3N.admin.catalogo.domain.genre.Genre;
import com.E3N.admin.catalogo.domain.genre.GenreGateway;
import com.E3N.admin.catalogo.domain.genre.GenreId;
import com.E3N.admin.catalogo.domain.validation.Error;
import com.E3N.admin.catalogo.domain.validation.ValidationHandler;
import com.E3N.admin.catalogo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase{

    private GenreGateway genreGateway;
    private CategoryGateway categoryGateway;

    public DefaultUpdateGenreUseCase(GenreGateway genreGateway, CategoryGateway categoryGateway) {
        this.genreGateway = genreGateway;
        this.categoryGateway = categoryGateway;
    }

    @Override
    public UpdateGenreOutput execute(UpdateGenreCommand updateGenreCommand) {
        final var genreId = GenreId.from(updateGenreCommand.id());
        var genre = genreGateway.findById(genreId)
                .orElseThrow(() -> NotFoundException.with(Genre.class, genreId));

        final var notification = Notification.create();
        final var ids = updateGenreCommand.categories().stream().map(CategoryID::from).toList();
        notification.append(validateCategories(ids));
        notification.validate(() -> genre.update(updateGenreCommand.name(), updateGenreCommand.isActive(), ids));
        if (notification.hasError()){
            throw new NotificationException("Could not update Aggregate Genre %s".formatted(updateGenreCommand.id()), notification);
        }
        return UpdateGenreOutput.from(genreGateway.update(genre));
    }

    private ValidationHandler validateCategories(List<CategoryID> ids){
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()){
            return notification;
        }

        final var retrievedIds = categoryGateway.existByIds(ids);
        if (ids.size() != retrievedIds.size()){
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);
            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s ".formatted(missingIdsMessage).trim()));
        }
        return  notification;
    }
}
