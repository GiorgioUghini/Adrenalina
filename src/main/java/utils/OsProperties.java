package utils;

public class OsProperties {

    static Boolean windows = null;

    public static boolean isWindows() {
        if (windows!=null){
            return windows;
        } else {
            windows = System.getProperty("os.name").startsWith("Windows");
            return windows;
        }
    }

}

