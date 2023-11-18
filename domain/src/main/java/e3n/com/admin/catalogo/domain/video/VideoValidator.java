package e3n.com.admin.catalogo.domain.video;

import e3n.com.admin.catalogo.domain.utils.StringUtils;
import e3n.com.admin.catalogo.domain.validation.Error;
import e3n.com.admin.catalogo.domain.validation.ValidationHandler;
import e3n.com.admin.catalogo.domain.validation.Validator;

public class VideoValidator extends Validator {

    private static final int TITLE_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4_000;
    private final Video video;

    protected VideoValidator(ValidationHandler handler, Video video) {
        super(handler);
        this.video = video;
    }

    @Override
    public void validate() {
        Error error = StringUtils.checkStringConstraints(this.video.getTitle(), TITLE_MAX_LENGTH, 1);
        if (error != null){
            this.validationHandler().append(error);
        }
        error = StringUtils.checkStringConstraints(this.video.getDescription(), DESCRIPTION_MAX_LENGTH, 1);
        if (error != null){
            this.validationHandler().append(error);
        }
        checkLaunchedAtConstraints();
        checkRatingConstraints();
    }

    private void checkLaunchedAtConstraints(){
        if (this.video.getLaunchedAt() == null){
            this.validationHandler().append(new Error("'launchedAt' shoul not be null"));
        }
    }

    private void checkRatingConstraints(){
        if (this.video.getRating() == null){
            this.validationHandler().append(new Error("'rating' shoul not be null"));
        }
    }
}
