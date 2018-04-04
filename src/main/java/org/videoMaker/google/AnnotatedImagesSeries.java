package org.videoMaker.google;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnnotatedImagesSeries {
    @JsonProperty("labels")
    private List<AnnotatedImages> annotatedImagesList;

    public List<AnnotatedImages> getAnnotatedImagesList() { return annotatedImagesList; }

    public void setAnnotatedImagesList(List<AnnotatedImages> annotatedImagesList) {
        this.annotatedImagesList = annotatedImagesList;
    }
}
