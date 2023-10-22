CREATE TABLE genre(
    id CHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NOT NULL
);

CREATE TABLE genre_category(
    genre_id CHAR(32) NOT NULL,
    category_id CHAR(32) NOT NULL,
    CONSTRAINT idx_genre_category UNIQUE (genre_id,category_id),
    CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genre (id) ON DELETE CASCADE,
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
);