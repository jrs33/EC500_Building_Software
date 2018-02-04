package org.videoMaker.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.vision.v1.AnnotateImageResponse;

import java.util.List;

public class AnnotatedImages {

    @JsonProperty("annotatedImageResponses")
    private List<List<AnnotateImageResponse>> annotatedImageResponses;

    public List<List<AnnotateImageResponse>> getAnnotatedImageResponses() {
        return annotatedImageResponses;
    }

    public void setAnnotatedImageResponses(List<List<AnnotateImageResponse>> annotatedImageResponses) {
        this.annotatedImageResponses = annotatedImageResponses;
    }
}
