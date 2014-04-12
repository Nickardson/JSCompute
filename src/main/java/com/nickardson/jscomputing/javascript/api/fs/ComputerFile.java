package com.nickardson.jscomputing.javascript.api.fs;

import java.util.List;

public abstract class ComputerFile {
    private List<ComputerFile> openFiles;

    public ComputerFile(List<ComputerFile> openFiles) {
        this.openFiles = openFiles;
        openFiles.add(this);
    }

    public void close() {
        openFiles.remove(this);
    }
}
