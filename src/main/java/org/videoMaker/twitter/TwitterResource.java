package org.videoMaker.twitter;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.joda.time.DateTime;
import org.videoMaker.mongo.LoggedResource;
import org.videoMaker.mongo.MongoLogger;
import org.videoMaker.sql.SQLogger;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/twitter")
public class TwitterResource implements LoggedResource {
    private static final String TWITTER_COLLECTION =  "twitter";

    private String consumerKey;
    private String consumerKeySecret;
    private String accessKey;
    private String accessKeySecret;
    private DB mongoDatabase;
    private SQLogger sqLogger;

    public TwitterResource(
            String consumerKey,
            String consumerKeySecret,
            String accessKey,
            String accessKeySecret,
            DB mongoDatabase,
            SQLogger sqLogger
    ) {
        this.consumerKey = consumerKey;
        this.consumerKeySecret = consumerKeySecret;
        this.accessKey = accessKey;
        this.accessKeySecret = accessKeySecret;
        this.mongoDatabase = mongoDatabase;
        this.sqLogger = sqLogger;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ImageAddresses getTweets() {
        ImageAddresses imageAddresses = new ImageAddresses();

        try {
            ConfigurationBuilder cb =
                    buildConfigurationObject(
                            consumerKey,
                            consumerKeySecret,
                            accessKey,
                            accessKeySecret
                    );
            Twitter twitter = buildTwitterAPIClient(cb);


            ResponseList<Status> responseList = retrieveTimelineTweets(twitter);
            List<String> imageList = getImagesFromTweets(responseList);

            imageAddresses = createImageAddressJSON(imageList);

            int numberImages = imageList.size();
            DBCollection collection = getTwitterCollection();
            log(
                    collection,
                    buildObject(numberImages)
            );
            sqLogger.logTwitter(DateTime.now(), numberImages);
        } catch (TwitterException te) {
            System.out.println(te.getErrorMessage());
        }

        return imageAddresses;
    }

    @Override
    public void log(DBCollection collection, BasicDBObject object) {
        try {
            MongoLogger.logInMongo(collection, object);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to insert in mongo " + TWITTER_COLLECTION);
        }
    }

    private BasicDBObject buildObject(int numberImages) {
        String date = DateTime.now().toString();
        BasicDBObject dbObject =
                new BasicDBObject("_id", date)
                        .append("numImages",numberImages);

        return dbObject;
    }

    private ConfigurationBuilder buildConfigurationObject(String consumerKey,
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

    private Twitter buildTwitterAPIClient(ConfigurationBuilder configurationBuilder) {
        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        Twitter twitter = twitterFactory.getInstance();

        return twitter;
    }

    private ResponseList<Status> retrieveTimelineTweets(Twitter twitterClient) {
        ResponseList<Status> responseList;
        try {
            responseList = twitterClient.getHomeTimeline();
        }
        catch (TwitterException te) {
            return null;
        }

        return responseList;
    }

    private List<String> getImagesFromTweets(ResponseList<Status> tweetJSONObject) {
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

    private ImageAddresses createImageAddressJSON(List<String> urlList) {
        ImageAddresses imageAddresses = new ImageAddresses();
        imageAddresses.setUrlList(urlList);

        return imageAddresses;
    }

    private DBCollection getTwitterCollection() {
        return mongoDatabase.getCollection(TWITTER_COLLECTION);
    }
}