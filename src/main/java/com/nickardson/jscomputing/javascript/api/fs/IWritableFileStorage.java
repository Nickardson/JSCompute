package com.nickardson.jscomputing.javascript.api.fs;

import java.io.FileNotFoundException;

public interface IWritableFileStorage extends IFileStorage {
    FileWritableJSAPI write(String path) throws FileNotFoundException;
    FileWritableJSAPI append(String path) throws FileNotFoundException;
}
