package org.videoMaker.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/*
    Representation of returned labels for individual images sent to the
    Google CV endpoint
 */
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
