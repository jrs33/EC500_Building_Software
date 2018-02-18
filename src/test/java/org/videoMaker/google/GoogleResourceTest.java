package org.videoMaker.google;

import com.fasterxml.jackson.databind.ObjectReader;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class GoogleResourceTest {

    private final String goodJSON =
            "{\"urlList\" : [\"http://pbs.twimg.com/ext_tw_video_thumb/965075590528122881/pu/img/8QaKikfwIfATp42n.jpg\"]}";
    private final String emptyJSON = "{\"urlList\" : []}";
    private final String brokenJSON =
            "{\"urlList\" : [\"http://pbs.tmg.com/extumb/96507551/u/mg/8QaKikfwIfATp42n.jpg\"]}";

    @Rule
    public final ResourceTestRule googleResourceTest = ResourceTestRule.builder()
            .addResource(new GoogleCVResource())
            .build();

    @Rule
    public Timeout timeout = Timeout.millis(10000);

    @Test
    public void googleReturnDataAsExpected() {
        Response response =
                googleResourceTest
                        .client()
                        .target("/google")
                        .request()
                        .post(Entity.entity(goodJSON, MediaType.APPLICATION_JSON_TYPE));

        System.out.println(response);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void googleHandlesEmptyData() {
        Response response =
                googleResourceTest
                        .client()
                        .target("/google")
                        .request()
                        .post(Entity.entity(emptyJSON, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void googleHandlesFalseLinks() {
        Response response =
                googleResourceTest
                        .client()
                        .target("/google")
                        .request()
                        .post(Entity.entity(brokenJSON, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void testGoogleEndpointTime() {
        long startTime = System.currentTimeMillis();

        googleResourceTest
                .client()
                .target("/google")
                .request()
                .post(Entity.entity(goodJSON, MediaType.APPLICATION_JSON_TYPE));

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("Google CV Endpoint Time(ms): " + duration);
    }

    @Test
    public void testGoogleMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        System.gc();

        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        googleResourceTest
                .client()
                .target("/google")
                .request()
                .post(Entity.entity(goodJSON, MediaType.APPLICATION_JSON_TYPE));

        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Memory Used By Google Call (bytes): " + (usedMemoryAfter - usedMemoryBefore));
    }

}
