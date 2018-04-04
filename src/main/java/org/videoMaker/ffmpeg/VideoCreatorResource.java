package org.videoMaker.ffmpeg;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.joda.time.DateTime;
import org.videoMaker.mongo.LoggedResource;
import org.videoMaker.mongo.MongoLogger;
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
public class VideoCreatorResource implements LoggedResource {
    private static final String FFMPEG_COLLECTION = "ffmpeg";

    private DB mongoDatabase;

    public VideoCreatorResource(
            DB mongoDatabase
    ) {
        this.mongoDatabase = mongoDatabase;
    }

    @Path("/saveImages")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void fileSaver(@QueryParam("outputPath") String imageOutputPath,
                            @DefaultValue("jpg") @QueryParam("fileExtension") String fileExtension,
                            ImageAddresses imageAddresses) {
        saveImagesToLocalFile(
                imageAddresses.getUrlList(),
                fileExtension,
                imageOutputPath
        );

        DBCollection collection = getffmpegCollection();
        BasicDBObject object = buildObject(imageAddresses.getUrlList(), fileExtension);
        log(collection, object);
    }

    @Path("/makeVideo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void videoCreator(@QueryParam("imagePath") String imagePath,
                               @QueryParam("videoPath") String videoPath,
                               @DefaultValue("jpg") @QueryParam("fileExtension") String fileExtension) {
        createAndSaveFFMPEGVideo(imagePath, videoPath, fileExtension);
    }

    @Override
    public void log(DBCollection collection, BasicDBObject object) {
        try {
            MongoLogger.logInMongo(collection, object);
        } catch (Exception e) {
            System.out.println("ERROR: unable to insert in mongo " + FFMPEG_COLLECTION);
        }
    }

    public BasicDBObject buildObject(List<String> imageAddresses, String format) {
        String date = DateTime.now().toString();
        BasicDBObject dbObject =
                new BasicDBObject("_id", date)
                        .append("imageUrls", imageAddresses)
                        .append("extension", format);

        return dbObject;
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
            Runtime.getRuntime().exec(ffmpegCommand);
        } catch (Exception e) {
            System.out.println("One of the paths was incorrect or ffmpeg cant generate video from listed images");
        }
    }

    private DBCollection getffmpegCollection() {
        return mongoDatabase.getCollection(FFMPEG_COLLECTION);
    }
}
