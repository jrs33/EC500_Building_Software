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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
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
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public void videoCreator(@QueryParam("imagePath") String imagePath,
                               @QueryParam("videoPath") String videoPath,
                               @DefaultValue("jpg") @QueryParam("fileExtension") String fileExtension) {
        createAndSaveFFMPEGVideo(imagePath, videoPath, fileExtension);

        try {
            java.nio.file.Path path = Paths.get(videoPath + "output.mp4");
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

            DBCollection collection = getffmpegCollection();
            BasicDBObject object = buildObject(attributes.size());
            log(collection, object);
        } catch (Exception e) {
            System.out.println("ERROR: unable to log data properly");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void log(DBCollection collection, BasicDBObject object) {
        try {
            MongoLogger.logInMongo(collection, object);
        } catch (Exception e) {
            System.out.println("ERROR: unable to insert in mongo " + FFMPEG_COLLECTION);
        }
    }

    private BasicDBObject buildObject(List<String> imageAddresses, String format) {
        String date = DateTime.now().toString();
        BasicDBObject dbObject =
                new BasicDBObject("_id", date)
                        .append("imageUrls", imageAddresses)
                        .append("extension", format)
                        .append("type", "saved");

        return dbObject;
    }

    private BasicDBObject buildObject(long size) {
        String date = DateTime.now().toString();
        BasicDBObject object =
                new BasicDBObject("_id", date)
                        .append("size", size)
                        .append("type", "video");

        return object;
    }

    private void saveImagesToLocalFile(List<String> imageAddresses,
                                      String fileExtension,
                                      String imageOutputPath) {
        Integer integerConcatenate = 0; // used to uniquely identify each saved image
        for (String url : imageAddresses) {
            try (InputStream in = new URL(url).openStream()) {
                String pathString = imageOutputPath + "image" + integerConcatenate.toString() + "." +fileExtension;
                OutputStream os = new FileOutputStream(pathString);

                byte[] b = new byte[2048];
                int length;
                while((length = in.read(b)) != -1) {
                    os.write(b, 0, length);
                }

                integerConcatenate = integerConcatenate + 1;
                os.close();
                in.close();
            } catch (Exception e) {
                System.out.println("Issue with request. Be sure you are adding a '/' on to the end of your path");
            }
        }
    }

    private void createAndSaveFFMPEGVideo(String imagePath,
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
