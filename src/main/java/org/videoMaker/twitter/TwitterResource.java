package org.videoMaker.twitter;

import org.videoMaker.client.ApplicationView;
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

    public TwitterResource() {}

    @GET
    @Path("{consumerKey}/{consumerKeySecret}/{accessToken}/{accessTokenSecret}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImageAddresses getTweets(@PathParam("consumerKey") String consumerKey,
                                    @PathParam("consumerKeySecret") String consumerKeySecret,
                                    @PathParam("accessToken") String accessToken,
                                    @PathParam("accessTokenSecret") String accessTokenSecret) {
        ImageAddresses imageAddresses = new ImageAddresses();

        try {
            ConfigurationBuilder cb =
                    buildConfigurationObject(
                            consumerKey,
                            consumerKeySecret,
                            accessToken,
                            accessTokenSecret
                    );
            Twitter twitter = buildTwitterAPIClient(cb);
            ResponseList<Status> responseList = retrieveTimelineTweets(twitter);
            List<String> imageList = getImagesFromTweets(responseList);

            imageAddresses = createImageAddressJSON(imageList);
        } catch (TwitterException te){
        }

        return imageAddresses;
    }

    public ConfigurationBuilder buildConfigurationObject(String consumerKey,
                                                         String consumerKeySecret,
                                                         String accessToken,
                                                         String accessTokenSecret) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerKeySecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        return cb;
    }

    public Twitter buildTwitterAPIClient(ConfigurationBuilder configurationBuilder) {
        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        Twitter twitter = twitterFactory.getInstance();

        return twitter;
    }

    public ResponseList<Status> retrieveTimelineTweets(Twitter twitterClient) {
        ResponseList<Status> responseList;
        try {
            responseList = twitterClient.getHomeTimeline();
        }
        catch (TwitterException te) {
            return null;
        }

        return responseList;
    }

    public List<String> getImagesFromTweets(ResponseList<Status> tweetJSONObject) {
        List<String> imageUris = new ArrayList<>();

        if(tweetJSONObject != null) {
            for (Status tweet : tweetJSONObject) {
                MediaEntity[] images = tweet.getMediaEntities();

                for (MediaEntity mediaEntity : images) {
                    if (!mediaEntity.getMediaURL().equals("")) {
                        imageUris.add(mediaEntity.getMediaURL());
                    }
                }
            }
        }

        return imageUris;
    }

    public ImageAddresses createImageAddressJSON(List<String> urlList) {
        ImageAddresses imageAddresses = new ImageAddresses();
        imageAddresses.setUrlList(urlList);

        return imageAddresses;
    }

}