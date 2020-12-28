package com.company.lab5;

import java.io.Serializable;
import java.util.ArrayList;

public class FileList implements Serializable {
    protected ArrayList <FileAtr> fileAtrArrayList;

    public FileList(FileAtr[] fileAtrs){
        fileAtrArrayList = new ArrayList<FileAtr>();
        for (FileAtr fileAtr : fileAtrs){
            fileAtrArrayList.add(fileAtr);
        }
    }

    public FileList(){
        super();
        fileAtrArrayList = new ArrayList<FileAtr>();
    }

    public ArrayList<FileAtr> getFileAtrArrayList() {
        return fileAtrArrayList;
    }

    public void addFile(FileAtr fileAtr){
        this.fileAtrArrayList.add(fileAtr);
    }

}
