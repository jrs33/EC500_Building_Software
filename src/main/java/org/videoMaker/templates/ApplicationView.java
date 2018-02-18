package org.videoMaker.templates;

import io.dropwizard.views.View;
import org.videoMaker.google.AnnotatedImages;
import org.videoMaker.twitter.ImageAddresses;

import java.util.List;

public class ApplicationView extends View {
    private ImageAddresses imageAddresses;
    private List<AnnotatedImages> annotatedImagesList;

    public ApplicationView(ImageAddresses imageAddresses,
                           List<AnnotatedImages> annotatedImagesList) {
        super("main_page.ftl");
        this.imageAddresses = imageAddresses;
        this.annotatedImagesList = annotatedImagesList;
    }

    public ImageAddresses getImageAddresses() {
        return imageAddresses;
    }

    public void setAnnotatedImagesList(List<AnnotatedImages> annotatedImagesList) {
        this.annotatedImagesList = annotatedImagesList;
    }

    public List<AnnotatedImages> getAnnotatedImagesList() {
        return annotatedImagesList;
    }

    public void setImageAddresses(ImageAddresses imageAddresses) {
        this.imageAddresses = imageAddresses;
    }
}
