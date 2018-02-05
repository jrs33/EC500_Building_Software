package org.videoMaker.google;

import com.google.cloud.vision.v1.*;
import org.videoMaker.twitter.ImageAddresses;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/google")
public class GoogleCVResource {

    /*
        Need to pass the API key for your service in the header of the request
    */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<AnnotatedImages> tagRemoteAddressedImageList(ImageAddresses imageAddresses) {
        List<AnnotatedImages> annotatedImagesList = new ArrayList<>();
        List<String> imageDescriptions = new ArrayList<>();

        for(String url : imageAddresses.getUrlList()) {
            ImageSource imageSource = ImageSource.newBuilder().setImageUri(url).build();
            Image image = Image.newBuilder().setSource(imageSource).build();
            Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            List<AnnotateImageRequest> requests = new ArrayList<>();

            requests.add(
                    buildImageRequestObject(feature,image)
            );

            imageDescriptions = googleCloudClientDescriptionFetcher(requests);

            annotatedImagesList.add(
                    createDescribedAnnotatedImageObject(imageDescriptions)
            );
        }

        return annotatedImagesList;
    }

    public List<String> googleCloudClientDescriptionFetcher(List<AnnotateImageRequest> requests) {
        List<AnnotateImageResponse> responses = new ArrayList<>();
        List<String> imageDescriptions = new ArrayList<>();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            responses = response.getResponsesList();

            imageDescriptions = addDescriptions(responses);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return imageDescriptions;
    }

    public List<String> addDescriptions(List<AnnotateImageResponse> annotateImageResponses) {
        List<String> imageDescriptions = new ArrayList<>();

        for(AnnotateImageResponse res : annotateImageResponses) {
            if (res.hasError()) {
                System.out.println("ERROR: Response has an error");
                break;
            }
            for (EntityAnnotation entityAnnotation : res.getLabelAnnotationsList()) {
                imageDescriptions.add(entityAnnotation.getDescription());
            }
        }

        return imageDescriptions;
    }

    public AnnotatedImages createDescribedAnnotatedImageObject(List<String> imageDescriptions) {
        AnnotatedImages annotatedImages = new AnnotatedImages();
        annotatedImages.setAnnotationDescriptions(imageDescriptions);

        return annotatedImages;
    }

    public AnnotateImageRequest buildImageRequestObject(Feature feature,
                                                        Image image) {
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();

        return request;
    }

}
