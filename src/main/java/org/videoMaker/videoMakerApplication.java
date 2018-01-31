package org.videoMaker;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.videoMaker.twitter.TwitterIF;
import org.videoMaker.twitter.TwitterResource;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;

import java.util.*;

import javax.xml.ws.Response;

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

        ResponseList<Status> timelineTweets = new ResponseList<Status>() {
            @Override
            public RateLimitStatus getRateLimitStatus() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Status> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Status status) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Status> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends Status> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Status get(int index) {
                return null;
            }

            @Override
            public Status set(int index, Status element) {
                return null;
            }

            @Override
            public void add(int index, Status element) {

            }

            @Override
            public Status remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<Status> listIterator() {
                return null;
            }

            @Override
            public ListIterator<Status> listIterator(int index) {
                return null;
            }

            @Override
            public List<Status> subList(int fromIndex, int toIndex) {
                return null;
            }

            @Override
            public int getAccessLevel() {
                return 0;
            }
        };
        List<String> images = new ArrayList<>();

        TwitterIF twitterIF = new TwitterIF.Impl(timelineTweets, images);

        environment.jersey().register(
                new TwitterResource(twitterIF)
        );

    }

}
