package org.videoMaker.client;

import io.dropwizard.views.View;
import org.videoMaker.google.AnnotatedImages;
import org.videoMaker.twitter.ImageAddresses;

import java.util.List;

public class ApplicationView extends View {
    private ImageAddresses imageAddresses;
    private AnnotatedImages annotatedImagesList;
    private String videoFilePath;

    public ApplicationView(ImageAddresses imageAddresses,
                           AnnotatedImages annotatedImagesList,
                           String videoFilePath) {
        super("main_page.ftl");
        this.imageAddresses = imageAddresses;
        this.annotatedImagesList = annotatedImagesList;
        this.videoFilePath = videoFilePath;
    }

    public ImageAddresses getImageAddresses() {
        return imageAddresses;
    }

    public AnnotatedImages getAnnotatedImagesList() {
        return annotatedImagesList;
    }

    public String getVideoFilePath() {
        return videoFilePath;
    }

}
