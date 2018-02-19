package org.videoMaker;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.dropwizard.views.ViewBundle;
import org.videoMaker.client.ViewCreator;
import org.videoMaker.ffmpeg.VideoCreatorResource;
import org.videoMaker.google.GoogleCVResource;
import org.videoMaker.twitter.TwitterResource;

import javax.ws.rs.client.Client;

public class videoMakerApplication extends Application<videoMakerConfiguration> {

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
    }

    @Override
    public void run(final videoMakerConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(
                new TwitterResource()
        );
        environment.jersey().register(
                new GoogleCVResource()
        );

        environment.jersey().register(
                new VideoCreatorResource()
        );

        final Client client = new JerseyClientBuilder(environment).build("DEMO_CLIENT");
        environment.jersey().register(new ViewCreator(client));
    }

}
