package nl.saxion.bd.opdracht1;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        String query = "{  call search_customer(?, ?)}";

        // zipcode
        Menu.print("Postcode:");
        String zipcode = scanner.next();
        //proc.setString(7, zipcode);

        Menu.print("Huisnummer:");
        String houseNumber = scanner.next();
        ResultSet rs = null;
        try {
            CallableStatement proc = c.prepareCall(query);
            proc.setString(1, zipcode);
            proc.setString(2, houseNumber);


            rs = proc.executeQuery();
            Menu.print("ID\t\t\tVoornaam\t\tAchternaam\t\tGeboortedatum\t\temail\t\tadres\t\thuis nummer\t\tpostcode\t\twoonplaats");
            while (rs.next())
            {
                // do something6
                Menu.print(rs.getString("id") + "\t\t\t" + rs.getString("first_name") + "\t\t" + rs.getString("last_name") +"\t\t" + rs.getString("date_of_birth")+
                       "\t\t"+ rs.getString("email") +"\t\t"+ rs.getString("address") +"\t\t" + rs.getString("housenumber") +"\t\t" +
                        rs.getString("zipcode") +"\t\t" + rs.getString("city"));

            }
            rs.close();
            proc.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
    void addAlbum() throws SQLException{
        Menu.print("ALBUM TOEVOEGEN");
        Menu.printStripes();

        try{
            // This process needs to commit on every update.
            c.setAutoCommit(true);
            String query = "{ ? = call new_album(?, ?, ?, ?)}";
            CallableStatement proc = c.prepareCall(query);
            proc.registerOutParameter(1, Types.INTEGER);

            // Album naam
            Menu.print("Naam van album:");
            String name = scanner.next();
            proc.setString(2, name);

            // reslease date
            Menu.print("Datum van uitgave: (dd-mm-jjjj)");
            Date releaseDate = addDate();
            proc.setDate(3, releaseDate);

            // Uitgever
            Menu.print("Uitgever:");
            proc.setString(4, searchPublisher());

            // Artiest id
            Menu.print("Artiest:");
            int artist = searchPerson();
            proc.setInt(5, artist);

            proc.execute();
            int album = proc.getInt(1);
            proc.close();

            // Nummers
            Menu.print("Nummers");
            Menu.print("Let op! De posities worden automatisch toegevoegd.");
            Menu.print("Zijn alle nummers van dezelfde artiest? [J/N]");
            boolean same = askYNQuestion();
            boolean add = true;
            for (int i = 1; add; i++) {
                String[] choices = new String[2];
                choices[0] = "Nummer toevoegen";
                choices[1] = "Stoppen";
                int choice = Menu.printChoices(choices, scanner);
                switch (choice){
                    case 0:
                        searchTrackWithArtist(album, same, artist, i);
                        break;
                    case 1:
                        add = false;
                        break;
                }
            }
            addAlbumCopy(album);
        } catch (Exception e){
            Menu.print(e.toString());
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        } finally {
            c.setAutoCommit(false);
        }
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
    }

    /**
     * Adds a copy to the database
     */
    void addCopy() {
        try{
            Menu.print("EXEMPLAAR TOEVOEGEN");
            Menu.print("Wilt u een film of album toevoegen (F/A):");
            while (true) {
                String choice = scanner.next();
                if (choice.equals("F")) {
                    // TODO: add film copy
                    break;
                } else if (choice.equals("A")) {
                    ArrayList<Integer> albums = searchAlbum();
                    Menu.print("Toets 0 voor opnieuw zoeken");
                    int trackChoice = scanner.nextInt();
                    switch (trackChoice){
                        case 0:
                            addCopy();
                            break;
                        default:
                            addAlbumCopy(albums.get(trackChoice - 1));
                            break;
                    }
                    break;
                } else {
                    Menu.print("Vul F(ilm) of A(lbum) in.");
                }
            }
            c.commit();
        } catch (Exception e){
            Menu.print(e.toString());
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
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
    int addPerson() {
        Menu.print("ACTEUR/ARTIEST TOEVOEGEN");
        Menu.printStripes();
        int id = 0;

        // Start query
        try{
            String query = "{ ? = call new_person(?, ?, ?)}";
            CallableStatement proc = c.prepareCall(query);
            proc.registerOutParameter(1, Types.INTEGER);

            // FirstName
            Menu.print("Voornaam:");
            String firstName = scanner.next();
            proc.setString(2, firstName);

            // LastName
            Menu.print("Achternaam:");
            String lastName = scanner.next();
            proc.setString(3, lastName);

            // dob
            Menu.print("Geboortedatum: (dd-mm-jjjj)");
            Date dob = addDate();
            proc.setDate(4, dob);

            proc.execute();
            id = proc.getInt(1);
            proc.close();
            c.commit();
        } catch (Exception e){
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
        return id;
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

    ArrayList<Integer> searchTrack(){
        Menu.print("NUMMER ZOEKEN");
        Menu.printStripes();
        ArrayList<Integer> tracks = null;
        try {
            String query = "{call search_track(?)}";
            CallableStatement st = c.prepareCall(query);
            Menu.print("Naam van track:");
            st.setString(1, scanner.next());

            ResultSet rs = st.executeQuery();
            tracks = printTracks(rs);
            rs.close();
            st.close();
        }  catch (Exception e){
            Menu.print(e.toString());
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
        return tracks;
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
     * Searches an album in the database
     */
    ArrayList<Integer> searchAlbum() {
        Menu.print("ALBUM ZOEKEN");
        Menu.printStripes();
        ArrayList<Integer> albums = new ArrayList<Integer>();;
        try {
            String query = "{call search_album(?)}";
            CallableStatement st = c.prepareCall(query);
            Menu.print("Naam van album:");
            st.setString(1, scanner.next());

            ResultSet rs = st.executeQuery();
            Menu.print("ID\t\t\tName\t\tUitgeef datum\tUitgever\t\tArtiest");
            int count = 1;
            while (rs.next())
            {
                albums.add(rs.getInt(1));
                Menu.print(count + "\t\t\t" + rs.getString(2) + "\t\t" + rs.getDate(3) + "\t\t\t" + rs.getString(4) + "\t\t" + rs.getString(5));
                count++;
            }
            rs.close();
            st.close();
        }  catch (Exception e){
            Menu.print(e.toString());
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
        return albums;
    }

    private String addEmail() {
        String email = "";
        Menu.print("E-mail toevoegen? [J/N]");
        boolean add = askYNQuestion();
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

    private int searchPerson() throws SQLException{
        // Getting artist
        Menu.print("Voornaam:");
        String firstName = scanner.next();
        Menu.print("Achternaam:");
        String lastName = scanner.next();
        PreparedStatement st = c.prepareStatement("SELECT * FROM person WHERE first_name LIKE ? AND last_name LIKE ?");
        st.setString(1, firstName);
        st.setString(2, lastName);

        ResultSet rs = st.executeQuery();
        Menu.print("ID\t\t\tVoornaam\t\tAchternaam\t\tGeboortedatum");
        while (rs.next())
        {
            Menu.print(rs.getInt(1) + "\t\t\t" + rs.getString(2) + "\t\t\t" + rs.getString(3) + "\t\t\t" + rs.getDate(4));
        }
        Menu.print("Toets -1 om een nieuw persoon toe te voegen");
        Menu.print("Toets 0 voor opnieuw");
        rs.close();
        st.close();
        Menu.print("Voer het ID in:");
        int choice = scanner.nextInt();
        int id = 0;
        switch (choice){
            case -1:
                id = addPerson();
                break;
            case 0:
                id = searchPerson();
                break;
            default:
                id = choice;
                break;
        }
        return id;
    }

    private String searchPublisher() throws SQLException{
        Menu.print("Naam van uitgever:");
        String name = scanner.next();

        PreparedStatement st = c.prepareStatement("SELECT * FROM publisher WHERE organisation LIKE ?");
        st.setString(1, name);
        ResultSet rs = st.executeQuery();
        Menu.print("Optie\t\tUitgever");
        Menu.print("-1\t\t\tNieuw");
        Menu.print("0\t\t\tOpnieuw");
        ArrayList<String> publishers = new ArrayList<String>();
        int count = 1;
        while (rs.next())
        {
            publishers.add(rs.getString(1));
            Menu.print(count + "\t\t\t" + rs.getString(1));
            count++;
        }
        rs.close();
        st.close();
        Menu.print("Voer het optie nummer van uw keuze in:");
        int choice = scanner.nextInt();
        String publish;
        switch (choice){
            case -1:
                publish = addPublisher();
                break;
            case 0:
                publish = searchPublisher();
                break;
            default:
                publish = publishers.get(choice-1);
                break;
        }
        return publish;
    }

    private String addPublisher() throws SQLException{
        String query = "{  call new_publisher(?)}";
        CallableStatement proc = c.prepareCall(query);

        Menu.print("Naam van uitgever:");
        String name = scanner.next();
        proc.setString(1, name);
        try {
            proc.execute();
        } catch(SQLException e){
            // If it is not succesfull, the publisher already exists.
        }
        proc.close();
        Menu.print("Toegevoegd!");
        return name;
    }

    private boolean askYNQuestion(){
        while (true) {
            String choice = scanner.next();
            if (choice.equals("J")) {
                return true;
            } else if(choice.equals("N")){
                return false;
            } else{
                Menu.print("Vul J of N in.");
            }
        }
    }

    private String addGenre() throws SQLException{
        String query = "{  call new_genre(?)}";
        CallableStatement proc = c.prepareCall(query);

        Menu.print("Naam van genre:");
        String name = scanner.next();
        proc.setString(1, name);
        try {
            proc.execute();
        } catch(SQLException e){
            // If it is not succesfull, the publisher already exists.
        }
        proc.close();
        return name;
    }

    private ArrayList<Integer> printTracks(ResultSet rs) throws SQLException{
        Menu.print("ID\t\t\tTitel\t\tTijdsduur\tArtiestnaam\t\t\tGenre");
        ArrayList<Integer> tracks = new ArrayList<Integer>();
        int count = 1;
        while (rs.next())
        {
            tracks.add(rs.getInt(1));
            Menu.print(count + "\t\t\t" + rs.getString(2) + "\t\t" + rs.getInt(3) + "\t\t\t" + rs.getString(4) + "\t\t" + rs.getString(5));
            count++;
        }
        return tracks;
    }

    private void searchTrackWithArtist(int album, boolean same, int artist, int pos) throws SQLException{
        String query = "{call search_track(?, ?)}";
        CallableStatement st = c.prepareCall(query);
        Menu.print("Naam van track:");
        st.setString(1, scanner.next());
        if (same){
            st.setInt(2, artist);
        } else {
            st.setInt(2, -1);
        }

        ResultSet rs = st.executeQuery();
        ArrayList<Integer> tracks = printTracks(rs);
        Menu.print("Toets -1 om een nieuwe track toe te voegen");
        Menu.print("Toets 0 voor opnieuw zoeken");
        rs.close();
        st.close();

        Menu.print("Voer het optie nummer van uw keuze in:");
        int choice = scanner.nextInt();
        switch (choice){
            case -1:
                addTrack(album, same, artist, pos);
                break;
            case 0:
                searchTrackWithArtist(album, same, artist, pos);
                break;
            default:
                query = "{ call combine_album_track(?, ?, ?) }";
                CallableStatement proc = c.prepareCall(query);

                proc.setInt(1, album);
                proc.setInt(2, tracks.get(choice-1));
                proc.setInt(3, pos);

                proc.execute();
                proc.close();
                break;
        }
    }

    private void addTrack(int album, boolean same, int artist, int pos) throws SQLException{

        String query = "{  call new_track(?, ?, ?, ?, ?, ?)}";
        CallableStatement proc = c.prepareCall(query);

        Menu.print("Title van track:");
        proc.setString(1, scanner.next());

        Menu.print("Lengte in seconde:");
        proc.setInt(2, scanner.nextInt());

        if (!same){
            artist = searchPerson();
        }
        proc.setInt(3, artist);

        proc.setString(4, addGenre());
        proc.setInt(5, album);
        proc.setInt(6, pos);

        proc.execute();
        proc.close();
    }

    private void addAlbumCopy(int album) throws SQLException{
        String query = "{  call add_album_copy(?)}";
        CallableStatement proc = c.prepareCall(query);

        proc.setInt(1, album);

        proc.execute();
        proc.close();
    }
}
