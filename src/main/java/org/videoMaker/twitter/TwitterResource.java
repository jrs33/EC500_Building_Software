package org.videoMaker.twitter;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/twitter")
public class TwitterResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseList<Status> getTweets() {

        // Creates a twitter4j configuration
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("");

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        // Gathering 20 responses
        ResponseList<Status> responseList;
        try {
            responseList = twitter.getHomeTimeline();
        }
        catch (TwitterException te) {
            return null;
        }

        return responseList;

    }

}