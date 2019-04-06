package controllers;

import java.io.File;

public class ResourceController {
    public static File getResource(String filename){
        ClassLoader classLoader = ResourceController.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }
}
