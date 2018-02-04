package org.videoMaker.google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.videoMaker.twitter.ImageAddresses;

import javax.imageio.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Path("/google")
public class GoogleCVResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<List<AnnotateImageResponse>> tagImages(ImageAddresses imageAddresses) {
        List<List<AnnotateImageResponse>> annotateImageResponses = new ArrayList<>();

        for(String url : imageAddresses.getUrlList()) {
            List<AnnotateImageRequest> requests = new ArrayList<>();

            ImageSource imageSource = ImageSource.newBuilder().setImageUri(url).build();
            Image image = Image.newBuilder().setSource(imageSource).build();
            Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
            requests.add(request);

            try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for(AnnotateImageResponse res : responses) {
                    if(res.hasError()) {
                        System.out.println(res);
                        System.out.println("Response has an error");
                    }
                }
                System.out.println(response);
                annotateImageResponses.add(responses);
            } catch (Exception e) {
                System.out.println("Unable to generate a client");
            }
        }
        AnnotatedImages annotatedImages = new AnnotatedImages();
        annotatedImages.setAnnotatedImageResponses(annotateImageResponses);

        return annotatedImages.getAnnotatedImageResponses();
    }

}
