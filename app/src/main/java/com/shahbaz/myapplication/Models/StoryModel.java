package com.shahbaz.myapplication.Models;

import android.net.Uri;

public class StoryModel {
    String name;
    String path;
    String fileName;
    Uri uri;

    public StoryModel() {
    }

    public StoryModel(String name, String path, String fileName, Uri uri) {
        this.name = name;
        this.path = path;
        this.fileName = fileName;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
