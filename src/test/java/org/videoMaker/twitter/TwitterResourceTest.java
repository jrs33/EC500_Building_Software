package org.videoMaker.twitter;

import com.fasterxml.jackson.databind.ObjectReader;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;

/*
Exercise error, expected behavior, performance (speed, memory required, etc.)
 */

public class TwitterResourceTest {

    @Rule
    public final ResourceTestRule resourceTestRule = ResourceTestRule.builder()
            .addResource(new TwitterResource())
            .build();

    @Rule
    public Timeout timeout = Timeout.millis(5000);

    @Test
    public void twitterReturnsDataAsExpected() throws IOException {
        String response = resourceTestRule.client().target(
                "/twitter/KbgvsecZyrsWq3bz97yuB2Blr/plLY9WJ3Wp9FrKFqk9MKMtGjP7hknACkrprE6ANgfyuomYGv0p/951789582-Tv3Rj5GfnH9v2ganaMYRFJn8tmzDcmrTBj5NZB63/NJvIItM8ZrKgcluyKVczNzPt0hUVnHcQOFtHqLgynBkZA"
        ).request().get(String.class);

        String missingJson = "{\"code\": 404, \"message\": \"HTTP 404 Not Found\"}";
        String emptyJson = "{}";

        ObjectReader objectReader = resourceTestRule.getObjectMapper().reader(ImageAddresses.class);

        // Serialize an object based on JSON
        ImageAddresses actual = objectReader.readValue(response);
        ImageAddresses empty = objectReader.readValue(emptyJson);

        assertThat(actual).isNotEqualTo(missingJson);
        assertThat(actual.getUrlList()).isNotEqualTo(empty.getUrlList());
    }

    @Test
    public void twitterFalseTokenRequest() {
        final String falseConsumerKey = "a";
        final String falseConsumerSecret = "b";
        final String falseAccessToken = "c";
        final String falseAccessSecret = "d";

        String expected = resourceTestRule.client().target(
                "/twitter/KbgvsecZyrsWq3bz97yuB2Blr/plLY9WJ3Wp9FrKFqk9MKMtGjP7hknACkrprE6ANgfyuomYGv0p/951789582-Tv3Rj5GfnH9v2ganaMYRFJn8tmzDcmrTBj5NZB63/NJvIItM8ZrKgcluyKVczNzPt0hUVnHcQOFtHqLgynBkZA"
        ).request().get(String.class);

        String actual = resourceTestRule.client().target(
                "/twitter/" + falseConsumerKey + "/" + falseConsumerSecret + "/" + falseAccessToken + "/" + falseAccessSecret
        ).request().get(String.class);

        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    public void twitterEndpointTime() {
        long startTime = System.currentTimeMillis();
        String expected = resourceTestRule.client().target(
                "/twitter/KbgvsecZyrsWq3bz97yuB2Blr/plLY9WJ3Wp9FrKFqk9MKMtGjP7hknACkrprE6ANgfyuomYGv0p/951789582-Tv3Rj5GfnH9v2ganaMYRFJn8tmzDcmrTBj5NZB63/NJvIItM8ZrKgcluyKVczNzPt0hUVnHcQOFtHqLgynBkZA"
        ).request().get(String.class);
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("Twitter Endpoint Time (ms): " + duration);
    }

    @Test
    public void twitterEndpointMemory() {
        Runtime runtime = Runtime.getRuntime();
        System.gc();

        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        resourceTestRule.client().target(
                "/twitter/KbgvsecZyrsWq3bz97yuB2Blr/plLY9WJ3Wp9FrKFqk9MKMtGjP7hknACkrprE6ANgfyuomYGv0p/951789582-Tv3Rj5GfnH9v2ganaMYRFJn8tmzDcmrTBj5NZB63/NJvIItM8ZrKgcluyKVczNzPt0hUVnHcQOFtHqLgynBkZA"
        ).request().get(String.class);
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Memory Used By Twitter Call (bytes): " + (usedMemoryAfter - usedMemoryBefore));
    }

}
