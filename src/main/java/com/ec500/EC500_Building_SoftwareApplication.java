package com.ec500;

import com.ec500.resources.TwitterResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EC500_Building_SoftwareApplication extends Application<EC500_Building_SoftwareConfiguration> {

    public static void main(final String[] args) throws Exception {
        new EC500_Building_SoftwareApplication().run(args);
    }

    @Override
    public String getName() {
        return "EC500_Building_Software";
    }

    @Override
    public void initialize(final Bootstrap<EC500_Building_SoftwareConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final EC500_Building_SoftwareConfiguration configuration,
                    final Environment environment) {

        environment.jersey().register(
                new TwitterResource()
        );

    }

}
