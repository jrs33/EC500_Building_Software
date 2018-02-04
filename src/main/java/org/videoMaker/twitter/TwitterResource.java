package org.videoMaker.twitter;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/twitter")
public class TwitterResource {

    @GET
    @Path("{consumerKey}/{consumerKeySecret}/{accessToken}/{accessTokenSecret}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImageAddresses getTweets(@PathParam("consumerKey") String consumerKey,
                                  @PathParam("consumerKeySecret") String consumerKeySecret,
                                  @PathParam("accessToken") String accessToken,
                                  @PathParam("accessTokenSecret") String accessTokenSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerKeySecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

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

        ImageAddresses imageAddresses = new ImageAddresses();
        imageAddresses.setUrlList(imageList);

        return imageAddresses;
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