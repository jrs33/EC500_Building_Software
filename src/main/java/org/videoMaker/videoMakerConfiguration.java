package org.videoMaker;

import com.meltmedia.dropwizard.mongo.MongoConfiguration;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class videoMakerConfiguration extends Configuration {
    @JsonProperty
    protected MongoConfiguration mongo;

    public MongoConfiguration getMongo() {
        return mongo;
    }
}
