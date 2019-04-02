package controllers;

import java.io.File;

public class ResourceController {
    public File getResource(String filename){
        ClassLoader classLoader = new ResourceController().getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        return file;
    }
}
