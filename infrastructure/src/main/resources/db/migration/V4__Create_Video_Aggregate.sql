CREATE TABLE video_video_media(
    id CHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    checksum VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    encoded_path VARCHAR(500) NOT NULL,
    media_status VARCHAR(50) NOT NULL
);

CREATE TABLE video_image_media(
    id CHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    checksum VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL
);

CREATE TABLE video(
    id CHAR(32) NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    year_launched SMALLINT NOT NULL,
    opened BOOLEAN NOT NULL DEFAULT FALSE,
    published BOOLEAN NOT NULL DEFAULT FALSE,
    rating VARCHAR(10),
    duration DECIMAL(5,2) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    video_id CHAR(32) NULL,
    trailer_id CHAR(32) NULL,
    banner_id CHAR(32) NULL,
    thumbnail_id CHAR(32) NULL,
    thumbnail_half_id CHAR(32) NULL,
    CONSTRAINT fk_v_video_id FOREIGN KEY (video_id) REFERENCES video_video_media (id) ON DELETE CASCADE,
    CONSTRAINT fk_v_trailer_id FOREIGN KEY (trailer_id) REFERENCES video_video_media (id) ON DELETE CASCADE,
    CONSTRAINT fk_v_banner_id FOREIGN KEY (banner_id) REFERENCES video_image_media (id) ON DELETE CASCADE,
    CONSTRAINT fk_v_thumbnail_id FOREIGN KEY (thumbnail_id) REFERENCES video_image_media (id) ON DELETE CASCADE,
    CONSTRAINT fk_v_thumbnail_half_id FOREIGN KEY (thumbnail_half_id) REFERENCES video_image_media (id) ON DELETE CASCADE
);

CREATE TABLE video_category(
    video_id CHAR(32) NOT NULL,
    category_id CHAR(32) NOT NULL,
    CONSTRAINT idx_vc_video_category UNIQUE (video_id, category_id),
    CONSTRAINT fk_vc_video_id FOREIGN KEY (video_id) REFERENCES video (id),
    CONSTRAINT fk_vc_category_id FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE video_genre(
    video_id CHAR(32) NOT NULL,
    genre_id CHAR(32) NOT NULL,
    CONSTRAINT idx_vg_video_genre UNIQUE (video_id, genre_id),
    CONSTRAINT fk_vg_video_id FOREIGN KEY (video_id) REFERENCES video (id),
    CONSTRAINT fk_vg_genre_id FOREIGN KEY (genre_id) REFERENCES genre (id)
);

CREATE TABLE video_cast_member(
    video_id CHAR(32) NOT NULL,
    cast_member_id CHAR(32) NOT NULL,
    CONSTRAINT idx_vcm_video_genre UNIQUE (video_id, cast_member_id),
    CONSTRAINT fk_vcm_video_id FOREIGN KEY (video_id) REFERENCES video (id),
    CONSTRAINT fk_vcm_genre_id FOREIGN KEY (cast_member_id) REFERENCES cast_member (id)
);