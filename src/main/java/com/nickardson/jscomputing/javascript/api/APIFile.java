package com.nickardson.jscomputing.javascript.api;

import com.nickardson.jscomputing.javascript.api.fs.ComputerFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class APIFile {

    public static String normalize(String file) {
        try {
            return new URI(file).normalize().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String combine(String file, String other) {
        try {
            return new URI(file + "/./" + other).normalize().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<ComputerFile> openFiles = new ArrayList<ComputerFile>();
}
