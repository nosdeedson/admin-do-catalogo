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
        checkTitleConstraints();
        checkDescriptionConstraints();
        checkLaunchedAtConstraints();
        checkRatingConstraints();
    }

    private void checkTitleConstraints(){
        final var title = this.video.getTitle();
        final var error = StringUtils.checkStringConstraints(title, "title", TITLE_MAX_LENGTH, 1);
        if (error != null){
            this.validationHandler().append(error);
        }
    }

    private void checkDescriptionConstraints(){
        if (this.video.getDescription().length() > DESCRIPTION_MAX_LENGTH){
            this.validationHandler().append(new Error("'description' should not be greater than 4000"));
        }
    }


    private void checkLaunchedAtConstraints(){
        if (this.video.getLaunchedAt() == null){
            this.validationHandler().append(new Error("'launchedAt' shoul not be null"));
        }
    }

    private void checkRatingConstraints(){
        if (this.video.getRating() == null){
            this.validationHandler().append(new Error("'rating' should not be null"));
        }
    }
}
