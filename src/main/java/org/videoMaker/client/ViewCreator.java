package org.videoMaker.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientResponse;
import org.videoMaker.google.AnnotatedImages;
import org.videoMaker.twitter.ImageAddresses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/createVideoUserInterface")
@Produces(MediaType.TEXT_HTML)
public class ViewCreator {
    /////
    /*
     * THESE NEED TO BE PUT IN A CONFIG.YAML
     */
    private final String consumerKey = "";
    private final String consumerSecret = "";
    private final String accessKey = "";
    private final String accessSecret = "";
    private final String imagePath = "/Users/joshsurette/Desktop/";
    private final String videoPath = "/Users/joshsurette/videoMaker/src/main/resources/org/videoMaker/client/";
    private final String host = "http://localhost:8080";
    /////

    private Client client;

    public ViewCreator(Client client) {
        this.client = client;
    }

    @GET
    public ApplicationView createLocalPage() {
        final String twitterPath =
                "/twitter/" + consumerKey + "/" + consumerSecret + "/" + accessKey + "/" + accessSecret;
        final String googlePath = "/google";
        final String saveImagesPath = "/ffmpeg/saveImages?outputPath=" + imagePath;
        final String makeVideoPath = "/ffmpeg/makeVideo?imagePath=" + imagePath + "&videoPath=" + videoPath;

        // TWITTER GET
        WebTarget twitterTarget = client.target(host + twitterPath);
        Invocation.Builder twitterInvocationBuilder = twitterTarget.request(MediaType.APPLICATION_JSON);
        Response response = twitterInvocationBuilder.get();
        ImageAddresses imageAddresses = response.readEntity(ImageAddresses.class);

        // TODO: Need to reform JSON since it is malformed in its return
        /* GOOGLE POST
        WebTarget googleTarget = client.target(host + googlePath);
        Invocation.Builder googleInvocationBuilder = googleTarget.request(MediaType.APPLICATION_JSON);
        Response googleResponse = googleInvocationBuilder.post(Entity.entity(imageAddresses, MediaType.APPLICATION_JSON));
        System.out.println(googleResponse);
        System.out.println(googleResponse.readEntity(AnnotatedImages.class));*/
        AnnotatedImages annotatedImages = new AnnotatedImages();

        // SAVE IMAGES AND MAKE VIDEO
        WebTarget saveImageTarget = client.target(host + saveImagesPath);
        Invocation.Builder saveImagesInvocation = saveImageTarget.request(MediaType.APPLICATION_JSON);
        saveImagesInvocation.post(Entity.entity(imageAddresses, MediaType.APPLICATION_JSON));

        WebTarget makeVideoTarget = client.target(host + makeVideoPath);
        Invocation.Builder makeVideoInvocation = makeVideoTarget.request(MediaType.APPLICATION_JSON);
        makeVideoInvocation.post(Entity.entity("",MediaType.APPLICATION_JSON));

        return new ApplicationView(imageAddresses, annotatedImages, videoPath+"output.mp4");
    }
}
