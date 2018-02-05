package org.videoMaker.ffmpeg;

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

    @Path("/saveImages")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String fileSaver(@QueryParam("outputPath") String imageOutputPath,
                               ImageAddresses imageAddresses) {
        saveImagesToLocalFile(
                imageAddresses.getUrlList(),
                imageOutputPath
        );

        return "Files saved to " + imageOutputPath + "!";
    }

    @Path("/makeVideo")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String videoCreator(@QueryParam("imagePath") String imagePath,
                               @QueryParam("videoPath") String videoPath,
                               @QueryParam("ffmpegPath") String ffmpegPath) {
        createAndSaveFFMPEGVideo(imagePath, videoPath, ffmpegPath);

        return "Video saved!";
    }

    public void saveImagesToLocalFile(List<String> imageAddresses,
                                      String imageOutputPath) {
        Image image = null;
        Integer integerConcatenate = 0; // used to uniquely identify each saved image
        for (String url : imageAddresses) {
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, Paths.get(imageOutputPath + "image" + integerConcatenate.toString() + ".jpg"));
                integerConcatenate = integerConcatenate + 1;
            } catch (Exception e) {
                System.out.println("Issue with request");
            }
        }
    }

    public void createAndSaveFFMPEGVideo(String imagePath,
                                         String videoPath,
                                         String ffmpegPath) {
        try {
            Runtime.getRuntime().exec("ffmpeg -r 1 -i image%d.png -s 320x240 -aspect 4:3 output.mp4");
        } catch (Exception e) {
            System.out.println("One of the paths was incorrect or ffmpeg cant generate video from listed images");
        }
    }

}
