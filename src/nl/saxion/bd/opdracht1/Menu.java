package nl.saxion.bd.opdracht1;

import java.util.Scanner;

/**
 * Created by Vincent on 12-2-2015.
 */
public class Menu {
    private Scanner scan;
    private DatabaseHandler dh;

    public Menu()
    {
        printLine("Opstarten...");
        scan = new Scanner(System.in);
        dh = new DatabaseHandler();
        // Initialisation goes here.
        boolean quit = false;
        printLine("Hallo!");
        printStripes();

        while (!quit) {
            int choice = showCommands();
            printStripes();
            switch (choice) {
                case 0:
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
        String[] choices = new String[7];
        choices[0] = "Quit";
        choices[1] = "Klanten";
        choices[2] = "Films + Albums";
        choices[3] = "Actueren";
        choices[4] = "Reserveringen";
        choices[5] = "Uitleningen";
        choices[6] = "Zoeken";
        return printChoices(choices);
    }

    public void customers()
    {
        printLine("KLANTEN");
        printStripes();
        String[] choices = new String[4];
        choices[0] = "Klant toevoegen";
        choices[1] = "Klant zoeken";
        choices[2] = "Klant gegevens wijzigen";
        choices[3] = "Terug";
        int choise = printChoices(choices);
        switch (choise){
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

    public void moviesAlbums()
    {
        printLine("FILMS + ALBUMS");
        printStripes();
        String[] choices = new String[5];
        choices[0] = "Film toevoegen";
        choices[1] = "Album toevoegen";
        choices[2] = "Exemplaar toevoegen";
        choices[3] = "Exemplaar wijzigen";
        choices[4] = "Terug";
        int choise = printChoices(choices);
        switch (choise){
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

    public void actors()
    {
        printLine("ARTIESTEN");
        printStripes();
        String[] choices = new String[3];
        choices[0] = "Artiest toevoegen";
        choices[1] = "Artiest wijzigen";
        choices[2] = "Terug";
        int choise = printChoices(choices);
        switch (choise){
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

    public void reservations()
    {
        printLine("RESERVATIES");
        printStripes();
        String[] choices = new String[2];
        choices[0] = "Reservering toevoegen";
        choices[1] = "Terug";
        int choise = printChoices(choices);
        switch (choise){
            case 0:
                dh.addReservation();
                break;
            case 2:
                return;
        }
    }

    public void loans()
    {
        printLine("LENINGEN");
        printStripes();
        String[] choices = new String[4];
        choices[0] = "Exemplaar uitlenen";
        choices[1] = "Exemplaar terugbrengen";
        choices[2] = "History bekijken";
        choices[3] = "Terug";
        int choise = printChoices(choices);
        switch (choise){
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

    public void search()
    {
        printLine("ZOEKEN");
        printStripes();
        String[] choices = new String[6];
        choices[0] = "Zoeken op acteur";
        choices[1] = "Zoeken op artiest";
        choices[2] = "Zoeken op film";
        choices[3] = "Zoeken op track";
        choices[4] = "Zoeken op album";
        choices[5] = "Terug";
        int choise = printChoices(choices);
        switch (choise){
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

    public int printChoices(String[] choices)
    {
        printLine("Kies één van de volgende commando's");
        for(int i = 0; i < choices.length; i++){
            printLine(i + " " + choices[i]);
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
        printLine("Voer een correct nummer in!");
        return printChoices(choices);
    }
}
