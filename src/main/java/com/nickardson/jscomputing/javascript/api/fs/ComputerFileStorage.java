package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import com.nickardson.jscomputing.javascript.api.APIFile;

import java.io.*;
import java.util.Arrays;

public class ComputerFileStorage {

    public static ComputerFileStorageJSAPI create(ServerTerminalComputer computer) {
        return new ComputerFileStorageJSAPI(computer);
    }

    public static class ComputerFileStorageJSAPI implements IWritableFileStorage {

        private ServerTerminalComputer computer;

        private ComputerFileStorageJSAPI(ServerTerminalComputer computer) {
            this.computer = computer;
        }

        public void closeAll() {
            while (APIFile.openFiles.size() > 0) {
                APIFile.openFiles.get(0).close();
            }
        }

        public Object combine(String file, String other) throws IOException {
            return JavaScriptEngine.convert(APIFile.combine(file, other), computer.getScope());
        }

        public boolean exists(String dir) throws FileNotFoundException {
            File f = getComputerFile(dir);
            return f != null && f.exists();
        }

        public boolean isDirectory(String dir) throws FileNotFoundException {
            File f = getComputerFile(dir);
            return f != null && f.isDirectory();
        }

        public boolean isFile(String dir) throws FileNotFoundException {
            File f = getComputerFile(dir);
            return f != null && f.isFile();
        }

        public FileReadableJSAPI read(String dir) throws FileNotFoundException {
            File file = getComputerFile(dir);
            if (file.exists() && file.isFile()) {
                try {
                    return new FileReadableJSAPI(computer, new FileInputStream(file));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public FileWritableJSAPI write(String dir) throws FileNotFoundException {
            File file = getComputerFile(dir);
            try {
                return new FileWritableJSAPI(new FileOutputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public FileWritableJSAPI append(String path) {
            return null;
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

        @Override
        public boolean isWritable() {
            return true;
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
         *
         * @param file The file to check.
         * @return True if safe, false if not, or if errored.
         */
        private boolean isSandboxed(File file) {
            try {
                File dir = getComputerDirectory();
                return file.exists() && (file.equals(dir) || file.getCanonicalPath().startsWith(dir.getCanonicalPath()));
            } catch (IOException e) {
                return false;
            }
        }

        /**
         * Gets a file from the current computer.
         *
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
    }
}
