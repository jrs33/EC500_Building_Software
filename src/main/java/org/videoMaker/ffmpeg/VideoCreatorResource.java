package org.videoMaker.ffmpeg;

import org.videoMaker.twitter.ImageAddresses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/ffmpeg")
public class VideoCreatorResource {

    public VideoCreatorResource() {}

    @Path("/saveImages")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void fileSaver(@QueryParam("outputPath") String imageOutputPath,
                            @DefaultValue("jpg") @QueryParam("fileExtension") String fileExtension,
                            ImageAddresses imageAddresses) throws Exception {
        File file = new File(imageOutputPath);

        if (!file.isDirectory()) {
            throw new Exception("BAD OUTPUT PATH");
        }

        saveImagesToLocalFile(
                imageAddresses.getUrlList(),
                fileExtension,
                imageOutputPath
        );
    }

    @Path("/makeVideo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void videoCreator(@QueryParam("imagePath") String imagePath,
                               @QueryParam("videoPath") String videoPath,
                               @DefaultValue("jpg") @QueryParam("fileExtension") String fileExtension) {
        createAndSaveFFMPEGVideo(imagePath, videoPath, fileExtension);
    }

    public void saveImagesToLocalFile(List<String> imageAddresses,
                                      String fileExtension,
                                      String imageOutputPath) {
        Image image = null;
        Integer integerConcatenate = 0; // used to uniquely identify each saved image
        for (String url : imageAddresses) {
            try (InputStream in = new URL(url).openStream()) {
                String pathString = imageOutputPath + "image" + integerConcatenate.toString() + "." +fileExtension;
                Files.copy(in, Paths.get(pathString));
                integerConcatenate = integerConcatenate + 1;
            } catch (Exception e) {
                System.out.println("Issue with request");
            }
        }
    }

    public void createAndSaveFFMPEGVideo(String imagePath,
                                         String videoPath,
                                         String fileExtension) {
        try {
            String ffmpegCommand =
                    "ffmpeg -r 1 -i " + imagePath + "image%d." + fileExtension + " -s 320x240 -aspect 4:3 "+ videoPath +"output.mp4";
            System.out.println(ffmpegCommand);
            Runtime.getRuntime().exec(ffmpegCommand);
        } catch (Exception e) {
            System.out.println("One of the paths was incorrect or ffmpeg cant generate video from listed images");
        }
    }

}
