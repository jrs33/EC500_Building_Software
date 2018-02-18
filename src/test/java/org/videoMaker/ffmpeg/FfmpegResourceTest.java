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

    private final String goodJSON =
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

        Response emptyResponse =
                resource.client()
                        .target("/ffmpeg/saveImages?outputPath=" + outputPath)
                        .request()
                        .post(Entity.entity(emptyJSON, MediaType.APPLICATION_JSON_TYPE));

        Response brokenResponse =
                resource.client()
                        .target("/ffmpeg/saveImages?outputPath=" + outputPath)
                        .request()
                        .post(Entity.entity(brokenJSON, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
        assertThat(emptyResponse.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
        assertThat(brokenResponse.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void testSavingImagesBadPath() {
        try {
            Response response =
                    resource.client()
                            .target("/ffmpeg/saveImages?outputPath=" + brokenOutputPath)
                            .request()
                            .post(Entity.entity(goodJSON, MediaType.APPLICATION_JSON_TYPE));
            fail("SHOULD THROW EXCEPTION WITH BAD PATH");
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("BAD OUTPUT PATH");
        }
    }



}
