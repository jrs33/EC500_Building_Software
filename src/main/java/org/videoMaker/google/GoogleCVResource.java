package org.videoMaker.google;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import javax.imageio.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Path("/google")
public class GoogleCVResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<List<AnnotateImageResponse>> tagImages(List<String> imageAddresses) {

        List<List<AnnotateImageResponse>> annotateImageResponses = new ArrayList<>();

        // Instantiates a client to talk to the Google CV REST API
        try(ImageAnnotatorClient cv = ImageAnnotatorClient.create()) {

            for(String url : imageAddresses) {

                try {
                    List<AnnotateImageResponse> imageResponse;

                    byte[] imageByteArray = createByteArrayFromImageURL(url);
                    ByteString imageInMemory = readImageDataIntoMemory(imageByteArray);

                    List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();
                    annotateImageRequests.add(
                            buildAnnotateImageRequest(imageInMemory)
                    );

                    imageResponse = getLabelDetectionResponse(
                            annotateImageRequests,
                            cv);

                    annotateImageResponses.add(imageResponse);
                }
                catch (Exception e) {
                    // TODO: Enter something here
                }

            }
        }
        catch (Exception e) {
            // TODO: Enter something here
        }
        return annotateImageResponses;
    }

    public byte[] createByteArrayFromImageURL(String url) throws Exception {
        URL address = new URL(url);
        BufferedImage image = ImageIO.read(address);
        ByteArrayOutputStream imageByteBuffer = new ByteArrayOutputStream();

        ImageIO.write(image, "jpg", imageByteBuffer);
        imageByteBuffer.flush();
        byte[] imageByteArray = imageByteBuffer.toByteArray();
        imageByteBuffer.close();

        return imageByteArray;
    }

    public ByteString readImageDataIntoMemory(byte[] imageByteArray) {
        ByteString memoryImage = ByteString.copyFrom(imageByteArray);

        return memoryImage;
    }

    public AnnotateImageRequest buildAnnotateImageRequest(ByteString imageBytes) {
        Image image = Image.newBuilder()
                .setContent(imageBytes)
                .build();
        Feature feature = Feature.newBuilder()
                .setType(Feature.Type.LABEL_DETECTION)
                .build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(image)
                .build();

        return request;
    }

    public List<AnnotateImageResponse> getLabelDetectionResponse(List<AnnotateImageRequest> annotateImageRequests,
                                                                 ImageAnnotatorClient imageAnnotatorClient) {
        BatchAnnotateImagesResponse response = imageAnnotatorClient.batchAnnotateImages(annotateImageRequests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        return responses;
    }

}
