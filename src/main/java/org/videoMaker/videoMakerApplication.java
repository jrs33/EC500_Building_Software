package org.videoMaker;

import com.meltmedia.dropwizard.mongo.MongoBundle;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.dropwizard.views.ViewBundle;
import org.skife.jdbi.v2.DBI;
import org.videoMaker.client.ViewCreator;
import org.videoMaker.ffmpeg.VideoCreatorResource;
import org.videoMaker.google.GoogleCVResource;
import org.videoMaker.sql.SQLogger;
import org.videoMaker.twitter.TwitterResource;

import javax.ws.rs.client.Client;

public class videoMakerApplication extends Application<videoMakerConfiguration> {

    private MongoBundle<videoMakerConfiguration> mongoBundle;

    public static void main(final String[] args) throws Exception {
        new videoMakerApplication().run(args);
    }

    @Override
    public String getName() {
        return "videoMaker";
    }

    @Override
    public void initialize(final Bootstrap<videoMakerConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(mongoBundle = MongoBundle.<videoMakerConfiguration>builder()
                .withConfiguration(videoMakerConfiguration::getMongo)
                .build());
    }

    @Override
    public void run(final videoMakerConfiguration configuration,
                    final Environment environment
    ) {
        final DB db = mongoBundle.getDB();
        final DBIFactory factory = new DBIFactory();
        final DBI sqlDb = factory.build(environment, configuration.getDatabaseFactory(), "mysql");
        final SQLogger sqLogger = sqlDb.onDemand(SQLogger.class);

        environment.jersey().register(
                new TwitterResource(
                        configuration.getConsumerKey(),
                        configuration.getConsumerKeySecret(),
                        configuration.getAccessKey(),
                        configuration.getAccessKeySecret(),
                        db,
                        sqLogger
                )
        );
        environment.jersey().register(
                new GoogleCVResource(
                        db,
                        sqLogger
                )
        );

        environment.jersey().register(
                new VideoCreatorResource(
                        db,
                        sqLogger
                )
        );

        final Client client = new JerseyClientBuilder(environment).build("DEMO_CLIENT");
        environment.jersey().register(new ViewCreator(client));
    }

}
