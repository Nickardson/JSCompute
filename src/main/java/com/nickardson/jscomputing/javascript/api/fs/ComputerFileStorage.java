package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.javascript.api.APIFile;

import java.io.*;

public class ComputerFileStorage {

    public static ComputerFileStorageJSAPI create(IComputer computer) {
        return new ComputerFileStorageJSAPI(computer);
    }

    public static class ComputerFileStorageJSAPI implements IWritableFileStorage {

        private IComputer computer;

        private ComputerFileStorageJSAPI(IComputer computer) {
            this.computer = computer;
        }

        public Object combine(String file, String other) throws IOException {
            return computer.convert(APIFile.combine(file, other));
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
            if (file != null && file.exists() && file.isFile()) {
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
        public FileWritableJSAPI append(String dir) {
            File file = getComputerFile(dir);
            try {
                return new FileWritableJSAPI(new FileOutputStream(file, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Object dir(String dir) {
            File f = new File(getComputerDirectory(), dir);
            try {
                if (exists(dir) && isSandboxed(f)) {
                    return computer.convert(f.list());
                }
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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
                //noinspection ResultOfMethodCallIgnored
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
                return file.equals(dir) || file.getCanonicalPath().startsWith(dir.getCanonicalPath());
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
            try {
                File f = new File(getComputerDirectory(), rel);
                if (isSandboxed(f)) {
                    return f;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
