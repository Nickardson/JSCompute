package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.javascript.api.APIFile;

public abstract class ComputerFile {

    private int id;

    public ComputerFile(int id) {
        this.id = id;
        APIFile.openFiles.add(this);
    }

    public int getComputerID() {
        return id;
    }

    public void close() {
        APIFile.openFiles.remove(this);
    }
}
