package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;

import java.io.FileNotFoundException;

/**
 * A MultiFileStorage which defines one writable file storage, which all
 */
public class WritableMultiFileStorage extends MultiFileStorage {
    public static WritableMultiFileStorageJSAPI create(ServerTerminalComputer computer, IFileStorage[] stores, IWritableFileStorage writable) {
        return new WritableMultiFileStorageJSAPI(computer, stores, writable);
    }

    public static class WritableMultiFileStorageJSAPI extends MultiFileStorageJSAPI implements IWritableFileStorage {
        IWritableFileStorage writable;

        WritableMultiFileStorageJSAPI(ServerTerminalComputer computer, IFileStorage[] stores, IWritableFileStorage writable) {
            super(computer, stores);
            this.writable = writable;
        }

        @Override
        public FileWritableJSAPI write(String path) throws FileNotFoundException {
            return writable.write(path);
        }

        @Override
        public FileWritableJSAPI append(String path) throws FileNotFoundException {
            return writable.append(path);
        }
    }
}
