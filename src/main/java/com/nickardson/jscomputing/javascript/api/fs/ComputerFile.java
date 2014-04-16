package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.javascript.api.APIFile;

public abstract class ComputerFile {
    public ComputerFile() {
        APIFile.openFiles.add(this);
    }

    public void close() {
        APIFile.openFiles.remove(this);
    }
}
