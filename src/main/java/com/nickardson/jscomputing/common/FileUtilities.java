package com.nickardson.jscomputing.common;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class FileUtilities {

    private static String getClassLocation() {
        String location = FileUtilities.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace('\\', '/');

        if (location.startsWith("file:/")) {
            location = location.substring("file:/".length());
        }

        return location;
    }

    public static File getJarFile() {
        String location = getClassLocation();

        int pow = location.indexOf('!');
        if (pow != -1) {
            location = location.substring(0, pow);
        }

        return new File(location);
    }

    public static boolean isJarFile() {
        return getClassLocation().indexOf('!') != -1;
    }

    public static boolean isJarDirectory(String path) {
        if (isJarFile()) {
            try {
                JarFile jar = new JarFile(getJarFile());
                ZipEntry entry = jar.getEntry(path);
                return entry != null && entry.isDirectory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL url = FileUtilities.class.getResource("/" + path);
                if (url != null) {
                    URI uri = url.toURI();
                    File classpath = new File(uri);
                    return classpath.isDirectory();
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static List<String> dir(final String path) {
        final List<String> ls = new ArrayList<String>();

        if (isJarFile()) {
            try {
                JarFile jar = new JarFile(getJarFile());

                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path + "/")) {
                        ls.add(name.substring(path.length()));
                    }
                }

                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL url = FileUtilities.class.getResource("/" + path);
                File classpath = new File(url.toURI());

                Files.walkFileTree(classpath.toPath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        String str = file.toString().replace('\\', '/');

                        int index = str.indexOf(path);
                        if (index != -1) {
                            str = str.substring(index + path.length());
                        }

                        ls.add(str);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ls;
    }
}
