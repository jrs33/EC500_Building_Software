package org.videoMaker.ffmpeg;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class FfmpegResourceTest {

    /*private final String goodJSON =
            "{\"urlList\" : [\"http://pbs.twimg.com/ext_tw_video_thumb/965075590528122881/pu/img/8QaKikfwIfATp42n.jpg\"]}";
    private final String emptyJSON = "{\"urlList\" : []}";
    private final String brokenJSON =
            "{\"urlList\" : [\"http://pbs.tmg.com/extumb/96507551/u/mg/8QaKikfwIfATp42n.jpg\"]}";
    private final String outputPath = "/Users/joshsurette/Desktop";
    private final String brokenOutputPath = "/Uses/joshsuret";

    @Rule
    public final ResourceTestRule resource = ResourceTestRule.builder()
            .addResource(new VideoCreatorResource())
            .build();

    @Rule
    public Timeout timeout = Timeout.millis(10000);

    @Test
    public void testSavingImages() {
        Response response =
                resource.client()
                        .target("/ffmpeg/saveImages?outputPath=" + outputPath)
                        .request()
                        .post(Entity.entity(goodJSON, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void testSavingEmptyJson() {
        Response emptyResponse =
                resource.client()
                        .target("/ffmpeg/saveImages?outputPath=" + outputPath)
                        .request()
                        .post(Entity.entity(emptyJSON, MediaType.APPLICATION_JSON_TYPE));

        assertThat(emptyResponse.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void testSavingBadJson() {
        Response brokenResponse =
                resource.client()
                        .target("/ffmpeg/saveImages?outputPath=" + outputPath)
                        .request()
                        .post(Entity.entity(brokenJSON, MediaType.APPLICATION_JSON_TYPE));

        assertThat(brokenResponse.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void testSavingImagesBadPath() {
        Response response =
                resource.client()
                        .target("/ffmpeg/saveImages?outputPath=" + brokenOutputPath)
                        .request()
                        .post(Entity.entity(goodJSON, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void testVideoCreator() {
        Response response =
                resource.client()
                        .target("/ffmpeg/makeVideo?outputPath=" + outputPath + "&videoPath=" + outputPath)
                        .request()
                        .post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void testVideoCreatorBadPaths() {
        Response response =
                resource.client()
                        .target("/ffmpeg/makeVideo?outputPath=" + brokenOutputPath + "&videoPath=" + brokenOutputPath)
                        .request()
                        .post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void testMemoryUsedSavingImages() {
        Runtime runtime = Runtime.getRuntime();
        System.gc();

        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        resource.client()
                .target("/ffmpeg/saveImages?outputPath=" + outputPath)
                .request()
                .post(Entity.entity(goodJSON, MediaType.APPLICATION_JSON_TYPE));
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("|   Memory Used By Image Saving (bytes): " + (usedMemoryAfter - usedMemoryBefore));
    }

    @Test
    public void testMemoryUsedMakingVideo() {
        Runtime runtime = Runtime.getRuntime();
        System.gc();

        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        resource.client()
                .target("/ffmpeg/makeVideo?outputPath=" + outputPath + "&videoPath=" + outputPath)
                .request()
                .post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("|   Memory Used By Video Creation (bytes): " + (usedMemoryAfter - usedMemoryBefore));
    }

    @Test
    public void testTimeToSaveImages() {
        long startTime = System.currentTimeMillis();
        resource.client()
                .target("/ffmpeg/saveImages?outputPath=" + outputPath)
                .request()
                .post(Entity.entity(goodJSON, MediaType.APPLICATION_JSON_TYPE));
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        System.out.println("|   Saving Images Time (ms): " + duration);
    }

    @Test
    public void testTimeToMakeVideo() {
        long startTime = System.currentTimeMillis();
        resource.client()
                .target("/ffmpeg/makeVideo?outputPath=" + outputPath + "&videoPath=" + outputPath)
                .request()
                .post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        System.out.println("|   Making Video Time (ms): " + duration);
    }*/

}
