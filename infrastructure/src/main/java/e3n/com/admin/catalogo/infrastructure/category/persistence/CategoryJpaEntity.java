package e3n.com.admin.catalogo.infrastructure.category.persistence;


import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.category.CategoryID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity(name = "Category") // se usar como a seguir '@Entity(name = "Category")' no repository não precisa usar CategoryJpaEntity basta usar Category
@Table(name = "category")
public class CategoryJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 4000, nullable = true)
    private String description;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, name = "created_at", columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(nullable = false, name = "updated_at", columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(nullable = true, name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public CategoryJpaEntity() {
    }

    private CategoryJpaEntity(String id, String name, String description, boolean active, Instant createdAt, Instant updatedAt, Instant deletedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static CategoryJpaEntity from( final Category category){
        return new CategoryJpaEntity(
            category.getId().getValue(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }

    public Category toAggregate(){
        return Category.with(
                CategoryID.from(getId()),
                getName(),
                getDescription(),
                isActive(),
                getCreatedAt(),
                getUpdatedAt(),
                getDeletedAt()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
