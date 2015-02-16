package nl.saxion.bd.opdracht1;

import java.util.Scanner;

/**
 * This file contains the choosemenu's. The menu is mend to guide the user through the program.
 * After the user made his choice, this class will call the right method of the database handler.
 * Created by Vincent on 12-2-2015.
 */
public class Menu {
    /** The scanner for reading the user input. */
    private Scanner scan;
    /** The handler for the database. */
    private DatabaseHandler dh;

    /** Constructor of menu. When constructed, it will automatically print the main menu. */
    public Menu()
    {
        print("Opstarten...");
        scan = new Scanner(System.in);
        dh = DatabaseHandler.getInstance();
        dh.connect();
        boolean quit = false;
        print("Hallo!");
        printStripes();

        while (!quit) {
            int choice = showCommands();
            printStripes();
            switch (choice) {
                case 0:
                    dh.disconnect();
                    quit = true;
                    break;
                case 1:
                    customers();
                    break;
                case 2:
                    moviesAlbums();
                    break;
                case 3:
                    actors();
                    break;
                case 4:
                    reservations();
                    break;
                case 5:
                    loans();
                    break;
                case 6:
                    search();
                    break;
                default:
            }
        }
        print("Quiting program");
    }

    /**
     * Method for printing stripes to make the console more readable.
     */
    public static void printStripes()
    {
        print("-----------------------------------");
    }

    /**
     * Cause System.out.println takes too much time to type. Only works on strings though.
     * @param print The string to be printed.
     */
    public static void print(String print)
    {
        System.out.println(print);
    }

    /**
     * Shows the main menu commands. And returns the user input.
     * @return A valid command of the user.
     */
    public int showCommands()
    {
        String[] choices = new String[7];
        choices[0] = "Quit";
        choices[1] = "Klanten";
        choices[2] = "Films + Albums";
        choices[3] = "Actueren";
        choices[4] = "Reserveringen";
        choices[5] = "Uitleningen";
        choices[6] = "Zoeken";
        return printChoices(choices, scan);
    }

    /**
     * Shows the sub-menu of customers.
     */
    public void customers()
    {
        print("KLANTEN");
        printStripes();
        String[] choices = new String[4];
        choices[0] = "Klant toevoegen";
        choices[1] = "Klant zoeken";
        choices[2] = "Klant gegevens wijzigen";
        choices[3] = "Terug";
        int choice = printChoices(choices, scan);
        switch (choice){
            case 0:
                dh.addCustomer();
                break;
            case 1:
                dh.searchCustomer();
                break;
            case 2:
                dh.updateCustomer();
                break;
            case 3:
                return;
        }
    }

    /**
     * Shows the sub-menu of albums.
     */
    public void moviesAlbums()
    {
        print("FILMS + ALBUMS");
        printStripes();
        String[] choices = new String[5];
        choices[0] = "Film toevoegen";
        choices[1] = "Album toevoegen";
        choices[2] = "Exemplaar toevoegen";
        choices[3] = "Exemplaar wijzigen";
        choices[4] = "Terug";
        int choice = printChoices(choices, scan);
        switch (choice){
            case 0:
                dh.addMovie();
                break;
            case 1:
                dh.addAlbum();
                break;
            case 2:
                dh.addCopy();
                break;
            case 3:
                dh.updateCopy();
            case 4:
                return;
        }
    }

    /**
     * Shows the sub-menu of actors.
     */
    public void actors()
    {
        print("ARTIESTEN");
        printStripes();
        String[] choices = new String[3];
        choices[0] = "Artiest toevoegen";
        choices[1] = "Artiest wijzigen";
        choices[2] = "Terug";
        int choice = printChoices(choices, scan);
        switch (choice){
            case 0:
                dh.addActor();
                break;
            case 1:
                dh.updateActor();
                break;
            case 2:
                return;
        }
    }

    /**
     * Shows the sub-menu of reservations.
     */
    public void reservations()
    {
        print("RESERVATIES");
        printStripes();
        String[] choices = new String[2];
        choices[0] = "Reservering toevoegen";
        choices[1] = "Terug";
        int choice = printChoices(choices, scan);
        switch (choice){
            case 0:
                dh.addReservation();
                break;
            case 2:
                return;
        }
    }

    /**
     * Shows the sub-menu of loans.
     */
    public void loans()
    {
        print("LENINGEN");
        printStripes();
        String[] choices = new String[4];
        choices[0] = "Exemplaar uitlenen";
        choices[1] = "Exemplaar terugbrengen";
        choices[2] = "History bekijken";
        choices[3] = "Terug";
        int choice = printChoices(choices, scan);
        switch (choice){
            case 0:
                dh.addLoan();
                break;
            case 1:
                dh.returnLoan();
                break;
            case 2:
                dh.getHistory();
                break;
            case 3:
                return;
        }
    }

    /**
     * Shows the sub-menu of search.
     */
    public void search()
    {
        print("ZOEKEN");
        printStripes();
        String[] choices = new String[6];
        choices[0] = "Zoeken op acteur";
        choices[1] = "Zoeken op artiest";
        choices[2] = "Zoeken op film";
        choices[3] = "Zoeken op track";
        choices[4] = "Zoeken op album";
        choices[5] = "Terug";
        int choice = printChoices(choices, scan);
        switch (choice){
            case 0:
                dh.searchActor();
                break;
            case 1:
                dh.searchArtist();
                break;
            case 2:
                dh.searchMovie();
                break;
            case 3:
                dh.searchTrack();
                break;
            case 4:
                dh.searchAlbum();
                break;
            case 5:
                return;
        }
    }

    /**
     * Prints the given choices and waits for user input.
     * After the user input is given, it will check whether it is valid.
     * The user input is valid if its bigger than 0 and smaller than the length of the list.
     * If the user input is not valid it will ask the user to retry.
     * @param choices The choices the user may choose from.
     * @return The valid user input
     */
    public static int printChoices(String[] choices, Scanner scan)
    {
        print("Kies één van de volgende commando's");
        for(int i = 0; i < choices.length; i++){
            print(i + " " + choices[i]);
        }
        int result = -1;
        if (scan.hasNextInt()){
            result = scan.nextInt();
            if (result < choices.length && result >= 0){
                printStripes();
                return result;
            }
        }
        scan = new Scanner(System.in);
        printStripes();
        print("Voer een correct nummer in!");
        return printChoices(choices, scan);
    }
}
