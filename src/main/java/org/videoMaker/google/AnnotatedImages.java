package org.videoMaker.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.vision.v1.EntityAnnotation;

import java.util.List;

public class AnnotatedImages {

    @JsonProperty("description")
    private List<String> annotationDescriptions;

    public List<String> getAnnotationDescriptions() {
        return annotationDescriptions;
    }

    public void setAnnotationDescriptions(List<String> annotationDescriptions) {
        this.annotationDescriptions = annotationDescriptions;
    }

}
