package com.nickardson.jscomputing.javascript.api.fs;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IFileStorage {
    Object combine(String a, String b) throws IOException;

    boolean exists(String path) throws FileNotFoundException;

    boolean isDirectory(String path) throws FileNotFoundException;

    boolean isFile(String path) throws FileNotFoundException;

    FileReadableJSAPI read(String path) throws FileNotFoundException;

    Object dir();

    Object dir(String path);

    boolean isWritable();
}
