package e3n.com.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

public record VideoResponse(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("year_launched") int yearLaunched,
        @JsonProperty("duration") double duration,
        @JsonProperty("opened") boolean opened,
        @JsonProperty("published") boolean published,
        @JsonProperty("rating") String rating,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("banner") ImageMediaResponse banner,
        @JsonProperty("thumbnail") ImageMediaResponse thumbnail,
        @JsonProperty("thumbnailHalf") ImageMediaResponse thumbnailHalf,
        @JsonProperty("video") AudioVideoMediaResponse video,
        @JsonProperty("trailer") AudioVideoMediaResponse traile,
        @JsonProperty("categories_id") Set<String> categoriesId,
        @JsonProperty("genres_id") Set<String> genresId,
        @JsonProperty("members_id") Set<String> membersId
) {
}
