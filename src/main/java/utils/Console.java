package utils;

import java.util.Scanner;

public class Console {

    private static final Scanner scanner  = new Scanner(System.in);

    public static void print(String message){
        System.out.print(message);
    }

    public static void println(String message){
        System.out.println(message);
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
