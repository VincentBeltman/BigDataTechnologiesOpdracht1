package nl.saxion.bd.opdracht1;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for the database.
 * The methods of this class will call the right procedure of the database.
 * Created by Vincent on 13-2-2015.
 */
public class DatabaseHandler {
    private static DatabaseHandler instance;
    private static Scanner scanner;
    protected DatabaseHandler() {

    }
    private Connection c;

    public static DatabaseHandler getInstance(){
        if(instance ==null){
            instance = new DatabaseHandler();
            scanner = new Scanner(System.in);
        }
        return instance;
    }

    public void connect()
    {
        System.out.print("Connecting");
        c = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BD-opdracht1","bdopdracht1" , "1234");

            System.out.println("Connection succesfully ");
            c.setAutoCommit(false);


        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Database conection fout");
        }



    }

    public void disconnect()
    {
        try
        {
            c.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Database conection closing error");
        }


    }



    /**
     * Adds a customer to the database
     */
    void addCustomer() {
        Menu.print("KLANT TOEVOEGEN");
        Menu.printStripes();

        // Start query
        try{
            String query = "{  call new_customer(?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement proc = c.prepareCall(query);

            // FirstName
            Menu.print("Voornaam:");
            String firstName = scanner.next();
            proc.setString(1, firstName);

            // LastName
            Menu.print("Achternaam:");
            String lastName = scanner.next();
            proc.setString(2, lastName);

            // Mail
            String email = addEmail();
            proc.setString(3, email);

            // dob
            Menu.print("Geboortedatum: (dd-mm-jjjj)");
            Date dob = addDate();
            proc.setDate(4, dob);

            // city
            Menu.print("Woonplaats:");
            String city = scanner.next();
            proc.setString(5, city);

            // address
            Menu.print("Straatnaam:");
            String address = scanner.next();
            proc.setString(6, address);

            // zipcode
            Menu.print("Postcode:");
            String zipcode = scanner.next();
            proc.setString(7, zipcode);

            // housenumber
            Menu.print("Huisnummer:");
            String houseNumber = scanner.next();
            proc.setString(8, houseNumber);

            proc.execute();
            proc.close();
            c.commit();
        } catch (Exception e){
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
    }

    /**
     * Searches for a customer in the database
     */
    void searchCustomer() {
        Menu.print("ZOEK KLANT");
        Menu.printStripes();

        // zipcode
        Menu.print("Postcode:");
        String zipcode = scanner.next();
        //proc.setString(7, zipcode);

        Menu.print("Huisnummer:");
        String houseNumber = scanner.next();
        //proc.setString(8, houseNumber);
    }

    /**
     * Updates a customer in the database
     */
    void updateCustomer() {
        Menu.print("UPDATE KLANT");
        Menu.printStripes();

    }

    /**
     * Adds an album to the database
     */
    void addAlbum() {
        Menu.print("ALBUM TOEVOEGEN");
        Menu.printStripes();

        try{
            String query = "{  call new_person(?, ?, ?)}";
            CallableStatement proc = c.prepareCall(query);

            // Album naam
            Menu.print("Naam van album:");
            String name = scanner.next();
            proc.setString(1, name);

            // reslease date
            Menu.print("Datum van uitgave: (dd-mm-jjjj)");
            Date releaseDate = addDate();
            proc.setDate(2, releaseDate);

            // reslease date
            Menu.print("Artiest:");
            String artiest = scanner.next();
            //TODO: search artists
            proc.setDate(3, releaseDate);

            proc.execute();
            proc.close();
            c.commit();
        } catch (Exception e){
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }



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
    void addPerson() {
        Menu.print("ACTEUR/ARTIEST TOEVOEGEN");
        Menu.printStripes();

        // Start query
        try{
            String query = "{  call new_person(?, ?, ?)}";
            CallableStatement proc = c.prepareCall(query);

            // FirstName
            Menu.print("Voornaam:");
            String firstName = scanner.next();
            proc.setString(1, firstName);

            // LastName
            Menu.print("Achternaam:");
            String lastName = scanner.next();
            proc.setString(2, lastName);

            // dob
            Menu.print("Geboortedatum: (dd-mm-jjjj)");
            Date dob = addDate();
            proc.setDate(3, dob);

            proc.execute();
            proc.close();
            c.commit();
        } catch (Exception e){
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
    }

    /**
     * Updates an actor in the database
     */
    void updatePerson() {
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

    private Date addDate() {
        String temp = scanner.next();
        Date dob = null;
        DateFormat format = new SimpleDateFormat("dd-mm-yyyy");
        try{
            dob = new Date(format.parse(temp).getTime());
        } catch (ParseException e){
            Menu.print("De datum was niet in het goede format.");
            Menu.print("Het format is: dd-mm-jjjj");
            dob = addDate();
        }
        return dob;
    }
}
