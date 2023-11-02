package com.example.smithproducoes;

public class Video {
    String url;
    String fileId;
    String name;
    public Video(String url, String name) {
        this.url = url;
        this.fileId = fileId;
        this.name = name;
    }

    public Video(String url, String fileId, String name) {
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

    @Override
    public String toString() {
        return "Video{" +
                "url='" + url + '\'' +
                ", fileId='" + fileId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
