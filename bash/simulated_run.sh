#!/bin/bash
url="http://localhost:8080"
twitter="/twitter"
google="/google"
ffmpeg="/ffmpeg"
saveImages="/saveImages"
makeVideo="/makeVideo"
outputPathQueryParam="?outputPath="
imagePathQueryParam="?imagePath="
videoPathQueryParam="videoPath="
path="/Users/joshsurette/videoMaker/bash/"


images=$(curl $url$twitter)
echo $images

descriptions=$(curl -d $images -H "Content-Type: application/json" -X POST $url$google)
echo $descriptions

$(curl -d $images -H "Content-Type: application/json" -X POST $url$ffmpeg$saveImages$outputPathQueryParam$path)
$(curl $url$ffmpeg$makeVideo$imagePathQueryParam$path&$videoPathQueryParam$path)

rm image*
rm *.mp4
