package nl.saxion.bd.opdracht1;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Mike on 10-2-2015.
 */
public class apl {
    public  static  void main(String [] args)
    {
        printLine("Starting up...");
        // Initialisation goes here.
        boolean quit = false;
        printLine("Good day!");
        printStripes();

        while (!quit) {
            int choice = showCommands();
            printStripes();
            switch (choice) {
                case 0:
                    quit = true;
                    break;
                default:
                    printLine("ERROR command not found.");
                    printLine("Please try again.");
                    break;
            }
        }
        printLine("Quiting program");
    }

    public static void printStripes()
    {
        printLine("-----------------------------------");
    }

    public static void printLine(String print)
    {
        System.out.println(print);
    }

    public static int showCommands()
    {
        printLine("Choose one of the following commands. Type its number and press enter:");
        printLine("0: Quit");
        Scanner scan = new Scanner(System.in);
        try{
            return scan.nextInt();
        } catch (InputMismatchException e){
            printStripes();
            printLine("Please enter an number!");
            return showCommands();
        }
    }
}
