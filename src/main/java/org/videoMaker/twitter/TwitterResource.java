package org.videoMaker.twitter;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/twitter")
public class TwitterResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MediaEntity[]> getTweets() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("ihNH0CeGDm1rxhouZmX0p0c92")
                .setOAuthConsumerSecret("5KG6d44elMKVo5qMJlh9G9Vt71e1miqjrqDr78eoisAqWy8sUl")
                .setOAuthAccessToken("951789582-Tv3Rj5GfnH9v2ganaMYRFJn8tmzDcmrTBj5NZB63")
                .setOAuthAccessTokenSecret("NJvIItM8ZrKgcluyKVczNzPt0hUVnHcQOFtHqLgynBkZA");

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        ResponseList<Status> responseList;
        try {
            responseList = twitter.getHomeTimeline();
        }
        catch (TwitterException te) {
            return null;
        }

        List<MediaEntity[]> imageList = getImagesFromTweets(responseList);

        return imageList;
    }

    public List<MediaEntity[]> getImagesFromTweets(ResponseList<Status> tweetJSONObject) {
        List<MediaEntity[]> imageUris = new ArrayList<>();

        for(Status tweet : tweetJSONObject) {
            MediaEntity[] images = tweet.getMediaEntities();

            if(images.length != 0) {
                imageUris.add(images);
            }
        }

        return imageUris;
    }

}