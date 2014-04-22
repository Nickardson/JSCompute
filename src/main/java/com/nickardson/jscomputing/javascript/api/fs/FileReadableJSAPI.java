package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.computers.IComputer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class FileReadableJSAPI extends ComputerFile {

    private IComputer computer;
    private Scanner scanner;

    /**
     * Creates an exposable readable file.
     * @param computer The computer whose scope is used to create.
     * @param stream The stream to read from.
     * @throws FileNotFoundException
     */
    public FileReadableJSAPI(IComputer computer, InputStream stream) throws FileNotFoundException {
        super(computer.getID());
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

        return computer.convert(result);
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public Object next() {
        return computer.convert(scanner.next());
    }

    public boolean hasNext(String pattern) {
        return scanner.hasNext(pattern);
    }

    public Object next(String pattern) {
        return computer.convert(scanner.next(pattern));
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public Object nextLine() {
        return computer.convert(scanner.nextLine());
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
