package nl.saxion.bd.opdracht1;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for the database.
 * The methods of this class will call the right procedure of the database.
 * Created by Vincent on 13-2-2015.
 */
public class DatabaseHandler {
    private Scanner scanner;
    DatabaseHandler() {
        scanner = new Scanner(System.in);
    }

    /**
     * Adds a customer to the database
     */
    void addCustomer() {
        Menu.print("KLANT TOEVOEGEN");
        Menu.printStripes();
        Menu.print("Voornaam:");
        String firstName = scanner.next();
        Menu.print("Achternaam:");
        String lastName = scanner.next();
        String email = addEmail();
        Menu.print("Postcode:");
        String zipcode = scanner.next();
        // TODO: dob city address housenumber zipcode controleren?
    }

    /**
     * Searches for a customer in the database
     */
    void searchCustomer() {
        Menu.print("ZOEK KLANTEN");
        Menu.printStripes();
        // TODO: waarop?
    }

    /**
     * Updates a customer in the database
     */
    void updateCustomer() {
        Menu.print("UPDATE KLANT");
        Menu.printStripes();
        // TODO: wat en hoe?
    }

    /**
     * Adds an album to the database
     */
    void addAlbum() {
        Menu.print("ALBUM TOEVOEGEN");
        Menu.printStripes();
        Menu.print("Naam van album:");
        String name = scanner.next();
        Menu.print("Datum van uitgave (jaar maand dag) [JJJJ-MM-DD]:");
        String releaseDate = scanner.next();
        Menu.print("Artiest:");
        String artiest = scanner.next();
        // TODO: Zoeken naar artiest en keuze menu laten zien?
        Menu.print("Uitegever:");
        String publisher = scanner.next();
        // TODO: Zoeken naar uitgever en keuze menu laten zien?
        // TODO: Tracks!
    }

    /**
     * Adds a movie to the databsae
     */
    void addMovie() {
        Menu.print("FILM TOEVOEGEN");
        Menu.printStripes();
        Menu.print("Naam van film:");
        String name = scanner.next();
        // TODO: datum van uitgave?
        Menu.print("Genre:");
        String genre = scanner.next();
        // TODO: Zoeken naar genre en keuze menu laten zien?
        Menu.print("Uitegever:");
        String publisher = scanner.next();
        // TODO: Zoeken naar uitgever en keuze menu laten zien?
        Menu.print("Acteuren:");
        boolean add = true;
        while (add) {
            String[] choices = new String[2];
            choices[0] = "Acteur toevoegen";
            choices[1] = "Stoppen";
            int choice = Menu.printChoices(choices, scanner);
            switch (choice){
                case 0:
                    // TODO: Zoeken naar acteur en keuze menu laten zien?
                    break;
                case 1:
                    add = false;
                    break;
            }
        }
        // TODO: Automatisch één copie toevoegen?
    }

    /**
     * Adds a copy to the database
     */
    void addCopy() {
        Menu.print("EXEMPLAAR TOEVOEGEN");
        Menu.printStripes();
        Menu.print("Naam van film/album:");
        String name = scanner.next();
        //TODO: zoeken naar film en keuze menu laten zien?
    }

    /**
     * Updates a copy in the database
     */
    void updateCopy() {
        Menu.print("EXEMPLAAR AANPASSEN");
        Menu.printStripes();
        Menu.print("Naam van film/album:");
        String name = scanner.next();
        // TODO: zoeken naar film en keuze menu laten zien?
        // TODO: Wat aanpassen?
    }

    /**
     * Adds an actor to the database
     */
    void addActor() {
        Menu.print("ACTEUR/ARTIEST TOEVOEGEN");
        Menu.printStripes();
        Menu.print("Voornaam:");
        String firstName = scanner.next();
        Menu.print("Achternaam:");
        String lastName = scanner.next();
        Menu.print("Geboorte datum:");
        String dob = scanner.next();
        // TODO: Add actor
    }

    /**
     * Updates an actor in the database
     */
    void updateActor() {
        Menu.print("ACTEUR AANPASSEN");
        Menu.printStripes();
        Menu.print("Naam van actuer/artiest:");
        String name = scanner.next();
        // TODO: zoeken naar film en keuze menu laten zien?
        // TODO: Wat aanpassen?
    }

    /**
     * Adds an reservation to the database
     */
    void addReservation() {
        Menu.print("RESERVATIE TOEVOEGEN");
        Menu.printStripes();
        Menu.print("Film/album naam:");
        String movieAlbum = scanner.next();
        // TODO: zoeken naar film en keuze menu laten zien
        Menu.print("Klant naam:");
        String customer = scanner.next();
        // TODO: zoeken naar klant en keuze menu laten zien
        // TODO: Reservatie en loan samenvoegen?
    }

    /**
     * Adds a loan to the database
     */
    void addLoan() {
        Menu.print("UITLENEN");
        Menu.printStripes();
        Menu.print("Film/album naam:");
        String movieAlbum = scanner.next();
        // TODO: zoeken naar film en keuze menu laten zien
        Menu.print("Klant naam:");
        String customer = scanner.next();
        // TODO: zoeken naar klant en keuze menu laten zien
        // TODO: Reservatie en loan samenvoegen?
    }

    /**
     * Adds a return date to the loan. This method is mend for returning copies.
     */
    void returnLoan() {
        Menu.print("UITLENING TERUG BRENGEN");
        Menu.printStripes();
        Menu.print("Klant naam:");
        String customer = scanner.next();
        // TODO: zoeken naar klant en keuze menu laten zien.
        // TODO: Bij kiezen klant lijst van reservaties zien.
    }

    /**
     * Retrieves the loan-history
     */
    void getHistory() {
        Menu.print("GESCHIEDENIS");
        Menu.printStripes();
        // TODO: Waarop zoeken?
    }

    /**
     * Searches an actor in the database
     */
    void searchActor() {
        Menu.print("ACTEUR ZOEKEN");
        Menu.printStripes();
        // TODO: Waarop zoeken?
    }

    /**
     * Searches an artist in the database
     */
    void searchArtist() {
        Menu.print("ARTIEST ZOEKEN");
        Menu.printStripes();
        // TODO: Waarop zoeken?
    }

    /**
     * Searches a movie in the database
     */
    void searchMovie() {
        Menu.print("FILM ZOEKEN");
        Menu.printStripes();
        // TODO: Waarop zoeken?
    }

    /**
     * Searches a track in the database
     */
    void searchTrack() {
        Menu.print("TRACK ZOEKEN");
        Menu.printStripes();
        // TODO: Waarop zoeken?
    }

    /**
     * Searches an album in the database
     */
    void searchAlbum() {
        Menu.print("ALBUM ZOEKEN");
        Menu.printStripes();
        // TODO: Waarop zoeken?
    }

    private String addEmail() {
        boolean good = false;
        boolean add = false;
        String email = "";
        while (!good) {
            Menu.print("E-mail toevoegen? [Y/N]");
            String choise = scanner.next();
            if (choise.equals("Y")) {
                add = true;
                good = true;
            } else if(choise.equals("N")){
                good = true;
            } else{
                Menu.print("Vul Y of N in.");
            }
        }
        while (add) {
            Menu.print("E-mail:");
            email = scanner.next();
            Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher m = p.matcher(email);
            if (m.matches()) {
                add = false;
            } else {
                Menu.print("Voer geldige E-mail in!");
            }
        }
        return email;
    }
}
