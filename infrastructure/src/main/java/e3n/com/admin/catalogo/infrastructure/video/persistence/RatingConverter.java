package e3n.com.admin.catalogo.infrastructure.video.persistence;

import e3n.com.admin.catalogo.domain.video.Rating;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {


    @Override
    public String convertToDatabaseColumn(Rating rating) {
        if(rating == null) return null;
        return rating.getName();
    }

    @Override
    public Rating convertToEntityAttribute(String dbInfo) {
        if (dbInfo == null) return null;
        return Rating.of(dbInfo).orElse(null);
    }
}
