package com.company.lab5;

import java.io.Serializable;

public class FileAtr implements Serializable {
    private int id;
    private String name;
    private String createdAt;
    private String author;
    private String path;

    public FileAtr(int id, String name, String createdAt, String author, String path){
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.author = author;
        this.path = path;
    }

    public FileAtr(String fileName) {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public String getPath() {
        return path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
