package org.videoMaker.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ImageAddresses {

    @JsonProperty("urlList")
    private List<String> urlList;

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

}
