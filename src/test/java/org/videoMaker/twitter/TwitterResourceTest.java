package org.videoMaker.twitter;

import com.fasterxml.jackson.databind.ObjectReader;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import javax.ws.rs.core.Response;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;

public class TwitterResourceTest {

    private final String PATH =
            "";

    @Rule
    public final ResourceTestRule resourceTestRule = ResourceTestRule.builder()
            .addResource(new TwitterResource())
            .build();

    @Rule
    public Timeout timeout = Timeout.millis(5000);

    @Test
    public void twitterReturnsDataAsExpected() throws IOException {
        Response response = resourceTestRule.client().target(PATH).request().get();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void twitterFalseTokenRequest() {
        final String falseConsumerKey = "a";
        final String falseConsumerSecret = "b";
        final String falseAccessToken = "c";
        final String falseAccessSecret = "d";

        String expected = "{\"urlList\":[]}";

        String actual = resourceTestRule.client().target(
                "/twitter/" + falseConsumerKey + "/" + falseConsumerSecret + "/" + falseAccessToken + "/" + falseAccessSecret
        ).request().get(String.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void twitterEndpointTime() {
        long startTime = System.currentTimeMillis();
        resourceTestRule.client().target(PATH).request().get(String.class);
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("|   Twitter Endpoint Time (ms): " + duration);
    }

    @Test
    public void twitterEndpointMemory() {
        Runtime runtime = Runtime.getRuntime();
        System.gc();

        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        resourceTestRule.client().target(PATH).request().get(String.class);
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("|   Memory Used By Twitter Call (bytes): " + (usedMemoryAfter - usedMemoryBefore));
    }

}
