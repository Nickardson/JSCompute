package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import com.nickardson.jscomputing.javascript.api.APIFile;
import org.mozilla.javascript.NativeArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Combines multiple (readonly) IFileStorages into one which references them all.
 */
public class MultiFileStorage {

    public static MultiFileStorageJSAPI create(ServerTerminalComputer computer, IFileStorage[] stores) {
        return new MultiFileStorageJSAPI(computer, stores);
    }

    public static class MultiFileStorageJSAPI implements IFileStorage {
        private ServerTerminalComputer computer;
        private IFileStorage[] stores;

        MultiFileStorageJSAPI(ServerTerminalComputer computer, IFileStorage[] stores) {
            this.computer = computer;
            this.stores = stores;
        }

        @Override
        public Object combine(String a, String b) throws IOException {
            return JavaScriptEngine.convert(APIFile.combine(a, b), computer.getScope());
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
                NativeArray array = (NativeArray) store.dir(path);
                for (int i = 0; i < array.getLength(); i++) {
                    ls.add(array.get(i));
                }
            }

            return JavaScriptEngine.getContext().newArray(computer.getScope(), Arrays.copyOf(ls.toArray(), ls.size(), Object[].class));
        }

        @Override
        public boolean isWritable() {
            return false;
        }
    }

}
