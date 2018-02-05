package org.videoMaker.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public AnnotatedImages tagImages(ImageAddresses imageAddresses) {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        // TODO: Change hard coded index to accept function input
        ImageSource imageSource = ImageSource.newBuilder().setImageUri(imageAddresses.getUrlList().get(0)).build();
        Image image = Image.newBuilder().setSource(imageSource).build();
        Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
        requests.add(request);

        List<AnnotateImageResponse> responses = new ArrayList<>();
        List<String> imageDescriptions = new ArrayList<>();
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            responses = response.getResponsesList();
            System.out.println("responses: " + responses);

            for(AnnotateImageResponse res : responses) {
                if(res.hasError()) {
                    System.out.println("Response has an error");
                }
                System.out.println("Entity annotations: " + res.getLabelAnnotationsList());
                for(EntityAnnotation entityAnnotation : res.getLabelAnnotationsList()) {
                    imageDescriptions.add(entityAnnotation.getDescription());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(imageDescriptions);

        AnnotatedImages annotatedImages = new AnnotatedImages();
        annotatedImages.setAnnotationDescriptions(imageDescriptions);

        return annotatedImages;

    }

}
