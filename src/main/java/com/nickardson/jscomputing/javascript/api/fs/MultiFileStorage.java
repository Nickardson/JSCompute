package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.javascript.api.APIFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Combines multiple (readonly) IFileStorages into one which references them all.
 */
public class MultiFileStorage {

    public static MultiFileStorageJSAPI create(IComputer computer, IFileStorage[] stores) {
        return new MultiFileStorageJSAPI(computer, stores);
    }

    public static class MultiFileStorageJSAPI implements IFileStorage {
        private IComputer computer;
        private IFileStorage[] stores;

        MultiFileStorageJSAPI(IComputer computer, IFileStorage[] stores) {
            this.computer = computer;
            this.stores = stores;
        }

        @Override
        public Object combine(String a, String b) throws IOException {
            return computer.convert(APIFile.combine(a, b));
        }

        @Override
        public boolean exists(String path) throws FileNotFoundException {
            for (IFileStorage store : stores) {
                if (store.exists(path)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isDirectory(String path) throws FileNotFoundException {
            for (IFileStorage store : stores) {
                if (store.isDirectory(path)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isFile(String path) throws FileNotFoundException {
            for (IFileStorage store : stores) {
                if (store.isFile(path)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public FileReadableJSAPI read(String path) throws FileNotFoundException {
            for (IFileStorage store : stores) {
                if (store.exists(path) && store.isFile(path)) {
                    return store.read(path);
                }
            }
            return null;
        }

        @Override
        public Object dir() {
            return dir(".");
        }

        @Override
        public Object dir(String path) {
            List<Object> ls = new ArrayList<Object>();

            for (IFileStorage store : stores) {
                List dir = (List) store.dir(path);
                if (dir != null) {
                    for (Object file : dir) {
                        ls.add(file);
                    }
                }
            }

            return computer.convert(ls.toArray());
        }

        @Override
        public boolean isWritable() {
            return false;
        }
    }

}
