package controllers;

import java.io.InputStream;

public class ResourceController {
    public static InputStream getResource(String filename){
        ClassLoader classLoader = ResourceController.class.getClassLoader();
        return classLoader.getResourceAsStream(filename);
    }
}