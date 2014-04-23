package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.computers.IComputer;

import java.io.*;

public class FileWritableJSAPI extends ComputerFile {
    PrintWriter writer;

    public FileWritableJSAPI(IComputer computer, OutputStream stream) throws FileNotFoundException {
        super(computer.getID());
        this.writer = new PrintWriter(stream);
    }

    @Override
    public void close() {
        super.close();
        writer.close();
    }

    public FileWritableJSAPI write(Object obj) {
        writer.print(obj);
        return this;
    }

    public FileWritableJSAPI writeln(Object x) {
        writer.println(x);
        return this;
    }

    public FileWritableJSAPI writeln() {
        writer.println();
        return this;
    }

    public FileWritableJSAPI flush() {
        writer.flush();
        return this;
    }

    public FileWritableJSAPI writef(String format, Object... args) {
        writer.printf(format, args);
        return this;
    }
}
