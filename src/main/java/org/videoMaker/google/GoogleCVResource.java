package org.videoMaker.google;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
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
import java.util.List;

@Path("/google")
public class GoogleCVResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<List<AnnotateImageResponse>> tagImages(List<String> imageAddresses) {
        try(ImageAnnotatorClient cv = ImageAnnotatorClient.create()) {

            for(String url : imageAddresses) {

                try {
                    byte[] imageByteArray = createByteArrayFromImageURL(url);
                    ByteString imageInMemory = readImageDataIntoMemory(imageByteArray);
                }
                catch (Exception e) {
                    // TODO: Enter something here
                }

            }

        }
        catch (Exception e) {
            // TODO: Enter something here
        }
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

}
