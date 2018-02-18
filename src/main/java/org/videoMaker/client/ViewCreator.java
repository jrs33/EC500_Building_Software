package org.videoMaker.client;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.videoMaker.templates.ApplicationView;
import org.videoMaker.twitter.ImageAddresses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/createVideoUserInterface")
public class ViewCreator {
    /////
    /*
    THESE NEED TO BE PUT IN A CONFIG.YAML
     */
    /////
    private final String imagePath = "/Users/joshsurette/Desktop/";
    private final String videoPath = imagePath;
    private final String host = "http://localhost:8080";
    /////
    /////
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

        WebTarget webTarget = client.target(host + twitterPath);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        ImageAddresses imageAddresses = response.readEntity(ImageAddresses.class);

        return new ApplicationView(imageAddresses, null);
    }
}
