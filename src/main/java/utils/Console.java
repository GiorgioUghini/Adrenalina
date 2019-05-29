package utils;

import java.util.Scanner;

public class Console {

    public enum COLOR {
        WHITE,
        BLU,
        RED,
        PURPLE,
        YELLOW,
        GREEN
    }

    private static final Scanner scanner  = new Scanner(System.in);

    public static void print(String message){
        System.out.print(message);
    }

    public static void println(String message){
        System.out.println(message);
    }

    public static void printColor(String message, COLOR color){
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

    public static int nextInt(){
        int n = scanner.nextInt();
        scanner.nextLine();
        return n;
    }

    public static String nextLine(){
        return scanner.nextLine();
    }
}
