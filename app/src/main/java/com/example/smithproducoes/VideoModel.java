package com.example.smithproducoes;

public class VideoModel {
    String url;
    String fileId;
    String name;

    public VideoModel(String url, String fileId, String name) {
        this.url = url;
        this.fileId = fileId;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
