package com.ec500.resources;

import com.ec500.twitter.CollectTweets;
import com.google.inject.Inject;
import twitter4j.ResponseList;
import twitter4j.Status;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/getTweets")
@Produces(MediaType.APPLICATION_JSON)
public class TwitterResource {

    private final CollectTweets collectTweets;

    @Inject
    public TwitterResource(CollectTweets collectTweets) {
        this.collectTweets = collectTweets;
    }

    @GET
    @Path("/getTweets")
    ResponseList<Status> retrieveTweets() {

        return collectTweets.getTweets();

    }

}
