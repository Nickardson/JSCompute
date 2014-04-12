package com.nickardson.jscomputing.javascript.api.fs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class FileWritableJSAPI extends ComputerFile {
    PrintWriter writer;

    public FileWritableJSAPI(List<ComputerFile> openFiles, File file) throws FileNotFoundException {
        super(openFiles);
        this.writer = new PrintWriter(file);
    }

    @Override
    public void close() {
        super.close();
        writer.close();
    }

    public void write(Object obj) {
        writer.print(obj);
    }

    public void write(String s) {
        writer.print(s);
    }

    public void writeln(Object x) {
        writer.println(x);
    }

    public void writeln(String x) {
        writer.println(x);
    }

    public void writeln() {
        writer.println();
    }

    public void flush() {
        writer.flush();
    }

    public void writef(String format, Object... args) {
        writer.printf(format, args);
    };
}
