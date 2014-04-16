package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class FileReadableJSAPI extends ComputerFile {

    private ServerTerminalComputer computer;
    private Scanner scanner;

    /**
     * Creates an exposable readable file.
     * @param computer The computer whose scope is used to create.
     * @param stream The stream to read from.
     * @throws FileNotFoundException
     */
    public FileReadableJSAPI(ServerTerminalComputer computer, InputStream stream) throws FileNotFoundException {
        super();
        this.computer = computer;
        this.scanner = new Scanner(stream);
    }

    @Override
    public void close() {
        super.close();
        scanner.close();
    }

    public Object readAll() {
        scanner.useDelimiter("\\Z");
        String result = scanner.next();
        scanner.reset();
        return JavaScriptEngine.convert(result, computer.getScope());
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public Object next() {
        return JavaScriptEngine.convert(scanner.next(), computer.getScope());
    }

    public boolean hasNext(String pattern) {
        return scanner.hasNext(pattern);
    }

    public Object next(String pattern) {
        return JavaScriptEngine.convert(scanner.next(pattern), computer.getScope());
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public Object nextLine() {
        return JavaScriptEngine.convert(scanner.nextLine(), computer.getScope());
    }

    public boolean hasNextBoolean() {
        return scanner.hasNextBoolean();
    }

    public boolean nextBoolean() {
        return scanner.nextBoolean();
    }

    public boolean hasNextNumber() {
        return scanner.hasNextDouble();
    }

    public double nextNumber() {
        return scanner.nextDouble();
    }
}
