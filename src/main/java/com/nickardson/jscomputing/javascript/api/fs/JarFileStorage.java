package com.nickardson.jscomputing.javascript.api.fs;

import com.nickardson.jscomputing.common.FileUtilities;
import com.nickardson.jscomputing.common.computers.ServerTerminalComputer;
import com.nickardson.jscomputing.javascript.JavaScriptEngine;
import com.nickardson.jscomputing.javascript.api.APIFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class JarFileStorage {

    /**
     * Creates a JarFileStorage with the given folders.
     * @param computer The computer.
     * @param subPath The sub-path inside of the jar.
     * @param dirPath The path on the computer.
     * @return The created JarFileStorage.
     */
    public static JarFileStorageFSAPI create(ServerTerminalComputer computer, String subPath, String dirPath) {
        return new JarFileStorageFSAPI(computer, subPath, dirPath);
    }

    public static class JarFileStorageFSAPI implements IFileStorage {

        ServerTerminalComputer computer;

        /**
         * The path within the JAR
         */
        String subPath;

        /**
         * The path within the computer.
         */
        String dirPath;

        /**
         * A mapping of dirPaths to JAR resource URLs
         */
        Map<String, URL> directory;

        JarFileStorageFSAPI(ServerTerminalComputer computer, String subPath, String dirPath) {
            this.computer = computer;
            this.subPath = subPath;
            this.dirPath = dirPath;
            this.directory = new HashMap<String, URL>();

            List<String> ls = FileUtilities.dir(subPath);
            for (String file : ls) {
                directory.put(APIFile.combine("/" + dirPath, file), FileUtilities.class.getResource("/" + subPath + file));
            }
        }

        /**
         * Gets the actual path on disk.
         * @param path The path on the computer.
         * @return The path on disk, or null if the path isn't contained.
         */
        private String getActualPath(String path) {
            if (path.startsWith(dirPath)) {
                return APIFile.combine(subPath.substring(dirPath.length()), path);
            } else {
                return null;
            }
        }

        @Override
        public Object combine(String a, String b) throws IOException {
            return JavaScriptEngine.convert(APIFile.combine(a, b), computer.getScope());
        }

        @Override
        public boolean exists(String path) throws FileNotFoundException {
            return directory.containsKey(APIFile.combine("/", path));
        }

        @Override
        public boolean isDirectory(String path) throws FileNotFoundException {
            String actual = getActualPath(path);
            return actual != null && FileUtilities.isJarDirectory(actual);
        }

        @Override
        public boolean isFile(String path) throws FileNotFoundException {
            return exists(path);
        }

        @Override
        public FileReadableJSAPI read(String path) throws FileNotFoundException {
            try {
                return new FileReadableJSAPI(computer, directory.get(APIFile.combine("/", path)).openStream());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
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

            String p = APIFile.combine("/", path + "/./");

            for (Map.Entry<String, URL> entry : directory.entrySet()) {
                try {
                    if (contains(p, APIFile.combine("/", entry.getKey() + "/./"))) {
                        ls.add(entry.getKey().substring(p.length()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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