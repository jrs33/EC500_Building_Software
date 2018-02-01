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
    public List<String> getTweets() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("");

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        ResponseList<Status> responseList;
        try {
            responseList = twitter.getHomeTimeline();
        }
        catch (TwitterException te) {
            return null;
        }

        List<String> imageList = getImagesFromTweets(responseList);

        return imageList;
    }

    public List<String> getImagesFromTweets(ResponseList<Status> tweetJSONObject) {
        List<String> imageUris = new ArrayList<>();

        for(Status tweet : tweetJSONObject) {
            MediaEntity[] images = tweet.getMediaEntities();

            for(MediaEntity mediaEntity : images) {
                if(!mediaEntity.getMediaURL().equals("")) {
                    imageUris.add(mediaEntity.getMediaURL());
                }
            }
        }

        return imageUris;
    }

}