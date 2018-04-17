package org.videoMaker;

import com.meltmedia.dropwizard.mongo.MongoConfiguration;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class videoMakerConfiguration extends Configuration {
    @JsonProperty
    private String consumerKey;

    protected String getConsumerKey() {
        return consumerKey;
    }

    @JsonProperty
    private String consumerKeySecret;

    protected String getConsumerKeySecret() {
        return consumerKeySecret;
    }

    @JsonProperty
    private String accessKey;

    protected String getAccessKey() {
        return accessKey;
    }

    @JsonProperty
    private String accessKeySecret;

    protected String getAccessKeySecret() {
        return accessKeySecret;
    }

    @JsonProperty
    private MongoConfiguration mongo;

    protected MongoConfiguration getMongo() {
        return mongo;
    }

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public void setDatabaseFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDatabaseFactory() {
        return database;
    }
}
