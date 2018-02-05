package org.videoMaker.ffmpeg;

import org.videoMaker.google.AnnotatedImages;
import org.videoMaker.twitter.ImageAddresses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/ffmpeg")
public class VideoCreatorResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String videoCreator(@QueryParam("outputPath") String imageOutputPath,
                               @QueryParam("videoOutputPath") String videoOutputPath,
                               ImageAddresses imageAddresses) {
        saveImagesToLocalFile(
                imageAddresses.getUrlList(),
                imageOutputPath
        );

        return "Video created!";
    }

    public void saveImagesToLocalFile(List<String> imageAddresses,
                                      String imageOutputPath) {
        Image image = null;
        Integer integerConcatenate = 1; // used to uniquely identify each saved image
        for (String url : imageAddresses) {
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, Paths.get(imageOutputPath + "image" + integerConcatenate.toString() + ".jpg"));
                integerConcatenate = integerConcatenate + 1;
            } catch (Exception e) {
                System.out.println("Issue with request");
            }
        }
    }

}
