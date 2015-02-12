package nl.saxion.bd.opdracht1;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Vincent on 12-2-2015.
 */
public class Menu {

    public Menu(){
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
                case 1:
                    //customers();
                    break;
                case 2:
                    //moviesAlbums();
                    break;
                case 3:
                    //tracks();
                    break;
                case 4:
                    //actors();
                    break;
                case 5:
                    //reservations();
                    break;
                case 6:
                    //loans();
                    break;
                case 7:
                    //genres();
                    break;
                case 8:
                    //search();
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

    public int showCommands()
    {
        printLine("Choose one of the following commands. Type its number and press enter:");
        printLine("0: Quit");
        printLine("1: Klanten");
        printLine("2: Films + Albums");
        printLine("3: Nummers");
        printLine("4: Actuer");
        printLine("5: Resrvering");
        printLine("6: Uitlening");
        printLine("7: Genres");
        printLine("8: Zoeken");
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
