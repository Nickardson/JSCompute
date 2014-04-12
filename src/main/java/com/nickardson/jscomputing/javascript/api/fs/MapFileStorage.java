package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import com.nickardson.jscomputing.javascript.api.APIFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MapFileStorage {

    public static MapFileStorageJSAPI create(ServerTerminalComputer computer, Map<String, URL> urlMap) {
        return new MapFileStorageJSAPI(computer, urlMap);
    }

    public static class MapFileStorageJSAPI implements IFileStorage {

        private ServerTerminalComputer computer;
        private Map<String, URL> urlMap;

        private MapFileStorageJSAPI(ServerTerminalComputer computer, Map<String, URL> urlMap) {
            this.computer = computer;
            this.urlMap = urlMap;
        }

        @Override
        public Object combine(String a, String b) throws IOException {
            return JavaScriptEngine.convert(APIFile.combine(a, b), computer.getScope());
        }

        @Override
        public boolean exists(String path) throws FileNotFoundException {
            return urlMap.containsKey(APIFile.normalize(path).replaceAll("(^[\\\\/]*)|([\\\\/]*$)", ""));
        }

        @Override
        public boolean isDirectory(String path) throws FileNotFoundException {
            return false;
        }

        @Override
        public boolean isFile(String path) throws FileNotFoundException {
            return false;
        }

        @Override
        public FileReadableJSAPI read(String path) throws FileNotFoundException {
            return null;
        }

        @Override
        public FileWritableJSAPI write(String path) throws FileNotFoundException {
            return null;
        }

        @Override
        public FileWritableJSAPI append(String path) {
            return null;
        }

        @Override
        public Object dir() {
            return dir(".");
        }

        private boolean contains(String dir, String path) throws URISyntaxException {
            return new URI(dir).normalize().equals(new URI(path + "/..").normalize());
        }

        @Override
        public Object dir(String path) {
            List<String> ls = new ArrayList<String>();
            for (Map.Entry<String, URL> entry : urlMap.entrySet()) {
                try {
                    if (contains(path, entry.getValue().getPath())) {
                        ls.add(entry.getValue().getFile());
                    }
                } catch (URISyntaxException ignored) {
                }
            }
            return JavaScriptEngine.getContext().newArray(computer.getScope(), Arrays.copyOf(ls.toArray(), ls.size(), Object[].class));
        }
    }
}
