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

    public static MapFileStorageJSAPI create(ServerTerminalComputer computer, Map<String, URL> urlMap, boolean isIndexable) {
        return new MapFileStorageJSAPI(computer, urlMap, isIndexable);
    }

    public static class MapFileStorageJSAPI implements IFileStorage {

        private ServerTerminalComputer computer;
        private Map<String, URL> urlMap;
        private boolean isIndexable;

        private MapFileStorageJSAPI(ServerTerminalComputer computer, Map<String, URL> urlMap, boolean isIndexable) {
            this.computer = computer;
            this.urlMap = urlMap;
            this.isIndexable = isIndexable;
        }

        @Override
        public Object combine(String a, String b) throws IOException {
            return JavaScriptEngine.convert(APIFile.combine(a, b), computer.getScope());
        }

        @Override
        public boolean exists(String path) throws FileNotFoundException {
            return urlMap.containsKey(getFileKey(path));
        }

        @Override
        public boolean isDirectory(String path) throws FileNotFoundException {
            // TODO implement
            return false;
        }

        @Override
        public boolean isFile(String path) throws FileNotFoundException {
            return exists(path);
        }

        @Override
        public FileReadableJSAPI read(String path) throws FileNotFoundException {
            try {
                return new FileReadableJSAPI(computer, urlMap.get(getFileKey(path)).openStream());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Object dir() {
            return dir(".");
        }

        @Override
        public Object dir(String path) {
            List<String> ls = new ArrayList<String>();

            if (isIndexable) {
                for (Map.Entry<String, URL> entry : urlMap.entrySet()) {
                    try {
                        if (contains(path, entry.getKey())) {
                            ls.add(entry.getKey());
                        }
                    } catch (URISyntaxException ignored) {
                    }
                }
            }

            return JavaScriptEngine.getContext().newArray(computer.getScope(), Arrays.copyOf(ls.toArray(), ls.size(), Object[].class));
        }

        @Override
        public boolean isWritable() {
            return false;
        }

        /**
         * Gets the key for the given file.
         * @param path The path to the file.
         * @return The key for the file, in the file map.
         */
        private String getFileKey(String path) {
            return removeEdgeSlash(APIFile.normalize(path));
        }

        private boolean contains(String dir, String path) throws URISyntaxException {
            return new URI(dir).normalize().equals(new URI(path + "/..").normalize());
        }

        /**
         * Removes preceding and trailing slashes from the given string.
         * @param path The string.
         */
        private String removeEdgeSlash(String path) {
            if (path != null) {
                return path.replaceAll("(^[\\\\/]*)|([\\\\/]*$)", "");
            } else {
                return null;
            }
        }
    }
}
