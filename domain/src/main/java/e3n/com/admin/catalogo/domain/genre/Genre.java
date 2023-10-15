package e3n.com.admin.catalogo.domain.genre;

import e3n.com.admin.catalogo.domain.AggregateRoot;
import e3n.com.admin.catalogo.domain.category.CategoryID;
import e3n.com.admin.catalogo.domain.utils.InstantUtils;
import e3n.com.admin.catalogo.domain.exceptions.NotificationException;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;
import e3n.com.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Genre extends AggregateRoot<GenreId> {

    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(
            GenreId genreId,
            String name,
            boolean active,
            List<CategoryID> categories,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt
    ) {
        super(genreId);
        this.name = name;
        this.active = active;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        selValidate();
    }

    public static Genre newGenre(final String name, final boolean isActive){
        final var id = GenreId.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(id, name, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    public static Genre with(
            GenreId genreId,
            String name,
            boolean active,
            List<CategoryID> categories,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt
    ){
        return new Genre(genreId, name, active, categories, createdAt, updatedAt, deletedAt);
    }

    public static Genre with(Genre genre){
        return new Genre(
                genre.id,
                genre.name,
                genre.active,
                genre.categories,
                genre.createdAt,
                genre.updatedAt,
                genre.deletedAt
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new GenreValidator(handler, this).validate();
    }

    public Genre update(
            final String name,
            final boolean isActive,
            final List<CategoryID> categories
    ){
        if (isActive){
            activate();
        } else {
            deactivate();
        }
        this.name = name;
        this.categories = new ArrayList<>(categories);
        this.updatedAt = InstantUtils.now();
        selValidate();
        return this;
    }

    public Genre deactivate(){
        if (this.deletedAt == null){
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre activate(){
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    private void selValidate(){
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasError()){
            throw new NotificationException("Failed to create a Aggreate Genre", notification);
        }
    }

    private Genre addCategory(final CategoryID categoryID){
        if (categoryID == null){
            return this;
        }
        this.categories.add(categoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    private Genre addCategories(final List<CategoryID> categories){
        if (categories == null || categories.isEmpty()){
            return this;
        }
        this.categories.addAll(categories);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    private Genre removeCategory(final CategoryID categoryID){
        if (categoryID == null){
            return this;
        }
        this.categories.remove(categoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

}
