package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("UnusedDeclaration")
public class APIFile {

    private ServerTerminalComputer computer;

    private List<ComputerFile> openFiles = new ArrayList<ComputerFile>();

    public APIFile(ServerTerminalComputer computer) {
        this.computer = computer;
    }

    public void closeAll() {
        while (openFiles.size() > 0) {
            openFiles.get(0).close();
        }
    }

    public Object combine(String file, String other) throws IOException {
        File f = new File(getComputerFile(file), other);
        String result = ".";
        if (isSandboxed(f)) {
            result = f.getCanonicalPath().substring(getComputerDirectory().getCanonicalPath().length());
            if (result.length() > 0) {
                result = result.replace('\\', '/');
            } else {
                result = "/";
            }
        }
        return JavaScriptEngine.convert(result, computer.getScope());
    }

    public boolean exists(String dir) {
        File f = getComputerFile(dir);
        if (f != null && f.exists()) {
            return true;
        }
        return false;
    }

    public boolean isDirectory(String dir) {
        File f = getComputerFile(dir);
        if (f != null && f.isDirectory()) {
            return true;
        }
        return false;
    }

    public boolean isFile(String dir) {
        File f = getComputerFile(dir);
        if (f != null && f.isFile()) {
            return true;
        }
        return false;
    }

    public Reading read(String dir) throws FileNotFoundException {
        return new Reading(this, getComputerFile(dir));
    }

    public Writing write(String dir) throws FileNotFoundException {
        return new Writing(this, getComputerFile(dir));
    }

    public Object dir(String dir) {
        File f = new File(getComputerDirectory(), dir);
        if (isSandboxed(f)) {
            Object[] ls = f.list();
            return JavaScriptEngine.getContext().newArray(computer.getScope(), Arrays.copyOf(ls, ls.length, Object[].class));
        } else {
            return null;
        }
    }

    public Object dir() {
        return dir(".");
    }

    private File getComputerDirectory() {
        File d = new File(new File(ComputerManager.getWorldDirectory(), "jscomputing"), Integer.toString(computer.getID()));
        if (!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    /**
     * Gets whether this file is safe to open.
     * @param file The file to check.
     * @return True if safe, false if not, or if errored.
     */
    private boolean isSandboxed(File file) {
        try {
            File dir = getComputerDirectory();
            return file.equals(dir) || file.getCanonicalPath().startsWith(dir.getCanonicalPath());
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Gets a file from the current computer.
     * @param rel The path of the file on the computer.
     * @return The file, or null if it either doesn't exist, or isn't sandboxed.
     */
    private File getComputerFile(String rel) {
        File f = new File(getComputerDirectory(), rel);
        if (isSandboxed(f)) {
            return f;
        } else {
            return null;
        }
    }

    public abstract class ComputerFile {
        private APIFile api;

        public ComputerFile(APIFile api) throws FileNotFoundException {
            this.api = api;
            api.openFiles.add(this);
        }

        public void close() {
            api.openFiles.remove(this);
        }
    }

    public class Reading extends ComputerFile {

        private Scanner scanner;

        public Reading(APIFile api, File file) throws FileNotFoundException {
            super(api);
            this.scanner = new Scanner(file);
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

    public class Writing extends ComputerFile {

        PrintWriter writer;

        public Writing(APIFile api, File file) throws FileNotFoundException {
            super(api);
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
        }
    }
}
