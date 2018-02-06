
# videoMaker

What the videoMaker API is
---

This is a pure REST API backend written in Java using the Dropwizard framework. Dropwizard allows you to deploy
scalable backend API's with many useful libraries, like Jersey and Jackson, that allow you to tie in URL
endpoints to java functions and represent JSON using Plain Old Java Objects (POJOS). Check out the official
documentation here: http://www.dropwizard.io/1.2.2/docs/getting-started.html

REST API Documentation
---

The videoMaker API has a simple function of grabbing tweets from a Twitter timeline, tagging those videos using the Google CV API, and then displaying those tagged images as a video using FFMPEG. I broke these major functions into their own REST resources:

#### /twitter/{consumerKey}/{consumerKeySecret}/{accessToken}/{accessTokenSecret}:
This allows you to make requests to an authenticated Twitter API using the provided keys and filling them into the brackets (ie: path parameters). When you are authenticated, the endpoint will return to you the image url's that are currently on your home timeline, and returns the urls in a pure JSON object via a GET request

#### /google?key=API_KEY:
This is the POST endpoint used to get descriptions for the images from the /twitter endpoint. You need to get an authenticated API service key, which you can read more about here https://cloud.google.com/vision/docs/auth. Once you are authenticated, pass this key as a Query parameter, like you see above. Also, be sure to pass the JSON of image urls from the /twitter GET request in the body of the /google POST request, and you will get a pure JSON object back with image annotations from the Google CV label annotator

#### /ffmpeg:
The ffmpeg resource has *two separate endpoints*. Be sure to download ffmpeg and make sure it is accessible from your local machines command line (ie: brew install ffmpeg should do the trick for Mac users that have Homebrew). The endpoints are described below:

* */ffmpeg/saveImages?outputPath=""&fileExtension=""*:
This is a POST request responsible for saving the images from the
    /twitter endpoint locally in the QueryParam outputPath location on the local computer. In this POST, you pass the
    JSON from the output of the /twitter endpoint into the body of the request, and you should see the images in
    your specified outputPath. Note that there is also a fileExtension QueryParameter, but this defaults to .jpg

* */ffmpeg/makeVideo?imagePath=""&videoPath=""&fileExtension=""*:
This is also a POST request that is responsible for  converting the locally saved images into a video. Here, you need to specify the imagePath where your images are saved and also provide the output videoPath where the video will be saved after the POST request is made. The fileExtension QueryParameter is again defaulted .jpg, but you can pass it if you have different image extensions


How to start the videoMaker application
---
Be sure to install Maven first, which resolved dependencies for you in a pom.xml file that describes repositories stored in the remote Maven repo (https://maven.apache.org/download.cgi). Along with that, you should also download Postman, which is a great developer tool to make requests to the above endpoints and allows you to see the data they produce (https://www.getpostman.com/). Once you have these two tools installed, do the following:

1. Clone the repository and navigate to that directory
2. Run `mvn clean package` to build your application. This builds a fat jar that can run your application
3. Start application with `java -jar target/videoMaker-1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter url `http://localhost:8080`. You should see a 404 not found error which means it is running!

Feel free to incorporate this backend API into any frontend application you are working on.
