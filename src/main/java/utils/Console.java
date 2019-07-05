package utils;

import java.util.Scanner;

public class Console {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prints a message
     * @param message the message to print
     */
    public static void print(String message) {
        System.out.print(message);
    }

    /**
     * prints a message and starts a new line
     * @param message
     */
    public static void println(String message) {
        System.out.println(message);
    }

    /**
     * Print a message of the provided color. Does start a new line
     * @param message the message to write
     * @param color the color of the message to write
     */
    public static void printColor(String message, COLOR color) {
        if (OsProperties.isWindows()) {
            print(message);
        } else {
            switch (color) {
                case BLU:
                    print("\u001B[34m" + message);
                    break;
                case RED:
                    print("\u001B[31m" + message);
                    break;
                case WHITE:
                    print(message);
                    break;
                case PURPLE:
                    print("\u001B[35m" + message);
                    break;
                case YELLOW:
                    print("\u001B[33m" + message);
                    break;
                case GREEN:
                    print("\u001B[32m" + message);
                    break;
            }
            print("\u001B[0m");
        }
    }

    /**
     * Scans the user input for an integer
     * @return the read integer
     */
    public static int nextInt() {
        int n = scanner.nextInt();
        scanner.nextLine();
        return n;
    }

    /**
     * reads a line
     * @return the line read
     */
    public static String nextLine() {
        return scanner.nextLine();
    }

    public enum COLOR {
        WHITE,
        BLU,
        RED,
        PURPLE,
        YELLOW,
        GREEN
    }
}
