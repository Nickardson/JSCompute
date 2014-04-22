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
    }
}
