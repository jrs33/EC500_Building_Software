package org.videoMaker;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.videoMaker.twitter.TwitterResource;

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
        // TODO: application initialization
    }

    @Override
    public void run(final videoMakerConfiguration configuration,
                    final Environment environment) {

        environment.jersey().register(
                new TwitterResource()
        );

    }

}
