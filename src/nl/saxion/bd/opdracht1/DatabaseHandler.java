package nl.saxion.bd.opdracht1;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
            //
            String firstName = scanner.nextLine().trim();
            proc.setString(1, firstName);

            // LastName
            Menu.print("Achternaam:");
            String lastName = scanner.nextLine().trim();
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
            scanner.nextLine();
            String city = scanner.nextLine().trim();
            proc.setString(5, city);

            // address
            Menu.print("Straatnaam:");
            String address = scanner.nextLine().trim();
            proc.setString(6, address);

            // housenumber
            Menu.print("Huisnummer:");
            String houseNumber = scanner.nextLine().trim();
            proc.setString(8, houseNumber);

            // zipcode
            Menu.print("Postcode:");
            String zipcode = scanner.nextLine().trim();
            proc.setString(7, zipcode);
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
    List<Customer>  searchCustomer() {
        List<Customer> customers = new ArrayList<Customer>();
        Menu.print("ZOEK KLANT");
        Menu.printStripes();
        String query = "{  call search_customer(?, ?)}";

        // zipcode
        Menu.print("Postcode:");
        String zipcode = scanner.next();

        Menu.print("Huisnummer:");
        String houseNumber = scanner.next();
        try {
            CallableStatement proc = c.prepareCall(query);
            proc.setString(1, zipcode);
            proc.setString(2, houseNumber);
            ResultSet rs = proc.executeQuery();
            Menu.print("ID\t\t\tVoornaam\t\tAchternaam\t\tGeboortedatum\t\temail\t\tadres\t\thuis nummer\t\tpostcode\t\twoonplaats");
            while (rs.next())
            {
                Customer c = new Customer(rs);
                customers.add(c);
                // do something6
                Menu.print(c.toString());
            }
            rs.close();
            proc.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * Updates a customer in the database
     */
    void updateCustomer() {
        Menu.print("UPDATE KLANT");
        Menu.printStripes();
        List<Customer> customers = null;
        customers = searchCustomerUntilResults();
        if(customers == null)
        {
            return;
        }
        Menu.print("Voer ID in");
        int customerId = scanner.nextInt();
        Customer chosenCustomer =null;
        for(Customer c : customers)
        {
            if(c.getId() == customerId)
            {
                chosenCustomer = c;
                break;
            }
        }
        if(chosenCustomer != null)
        {   String query = "{  call update_customer(?, ? , ? , ? ,? ,? , ? ,?)}";
            try {
                CallableStatement proc = c.prepareCall(query);
                Menu.print("Druk op Enter om de huidige waarde ongewijzigd te laten");
                Menu.print("Voornaam: " + chosenCustomer.getFirstName());
                //ignore first /n
                scanner.nextLine();
                String newFirstName = scanner.nextLine().trim();
                if(!newFirstName.isEmpty() )
                {
                    chosenCustomer.setFirstName(newFirstName);
                }

                proc.setString(1 ,chosenCustomer.getFirstName());

                Menu.print("Achternaam: " + chosenCustomer.getLastName());
                String newLastname = scanner.nextLine().trim();
                if(!newLastname.isEmpty())
                {
                    chosenCustomer.setLastName(newLastname);
                }
                proc.setString(2 , chosenCustomer.getLastName());
                Menu.print("Email: " +  chosenCustomer.getEmail());
                String newEmail = scanner.nextLine().trim();
                if(!newEmail.isEmpty()) {
                    chosenCustomer.setEmail(newEmail);
                }
                proc.setString(3, chosenCustomer.getEmail());

                Menu.print("Adres: " + chosenCustomer.getAddress());
                String newAddress = scanner.nextLine().trim();
                if(!newAddress.isEmpty())
                {
                    chosenCustomer.setAddress(newAddress);
                }
                proc.setString(4 , chosenCustomer.getAddress());
                Menu.print("Huisnummer + toevoeging: " + chosenCustomer.getHousenumber());
                String newHousenumber = scanner.nextLine().trim();
                if(!newHousenumber.isEmpty())
                {
                    chosenCustomer.setHousenumber(newHousenumber);
                }
                proc.setString(5, chosenCustomer.getHousenumber());

                Menu.print("Postcode: " + chosenCustomer.getZipcode() );
                String newZipcode = scanner.nextLine().trim();
                if(!newZipcode.isEmpty())
                {
                    chosenCustomer.setZipcode(newZipcode);
                }
                proc.setString(6 , chosenCustomer.getZipcode());

                Menu.print("Stad: " + chosenCustomer.getCity() );
                String newCity = scanner.nextLine().trim();
                if(!newCity.isEmpty())
                {
                    chosenCustomer.setCity(newCity);
                }
                proc.setString(7, chosenCustomer.getCity());

                proc.setInt(8, chosenCustomer.getId());
                if(proc.execute())
                {
                    Menu.print("Klant Succesvol aangepast");
                }
                c.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Menu.print("FOUT verkeerd id meegegeven");
        }
    }
    /**
     * Adds an album to the database
     */
    void addAlbum() throws SQLException{
        Menu.print("ALBUM TOEVOEGEN");
        Menu.printStripes();

        try{
            // This process needs to commit on every update.
            String query = "{ ? = call new_album(?, ?, ?, ?)}";
            CallableStatement proc = c.prepareCall(query);
            proc.registerOutParameter(1, Types.INTEGER);

            // Album naam
            Menu.print("Naam van album:");
            proc.setString(2, scanner.next());

            // reslease date
            Menu.print("Datum van uitgave: (dd-mm-jjjj)");
            proc.setDate(3, addDate());

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
        } catch (Exception e){
            Menu.print(e.toString());
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
    }

    /**
     * Adds a movie to the databsae
     */
    void addMovie() {
        Menu.print("FILM TOEVOEGEN");
        Menu.printStripes();

        try{
            String query = "{ ? = call new_movie(?, ?, ?, ?, ?)}";
            CallableStatement proc = c.prepareCall(query);
            proc.registerOutParameter(1,Types.INTEGER);

            // Album naam
            Menu.print("Titel van film:");
            proc.setString(2, scanner.nextLine());

            // Album naam
            Menu.print("Regiseur:");
            proc.setInt(3, searchPerson());

            // Uitgever
            Menu.print("Uitgever:");
            proc.setString(4, searchPublisher());

            // Genre
            Menu.print("Genre:");
            proc.setString(5, addGenre());

            // Director
            Menu.print("Datum van uitgave: (dd-mm-jjjj)");
            proc.setDate(6, addDate());

            proc.execute();
            int movie_id = proc.getInt(1);
            proc.close();

            // Actors
            Menu.print("Acteuren:");
            boolean add = true;
            for (int i = 1; add; i++) {
                String[] choices = new String[2];
                choices[0] = "Acteur toevoegen";
                choices[1] = "Stoppen";
                int choice = Menu.printChoices(choices, scanner);
                switch (choice){
                    case 0:
                        query = "{ call combine_movie_artist(?, ?, ?)}";
                        proc = c.prepareCall(query);

                        proc.setInt(1, movie_id);
                        proc.setInt(2, searchPerson());
                        Menu.print("Rol naam:");
                        Menu.print(scanner.nextLine().length() + "");
                        proc.setString(3, scanner.nextLine());

                        proc.execute();
                        proc.close();
                        break;
                    case 1:
                        add = false;
                        break;
                }
            }
            c.commit();
        } catch (Exception e){
            Menu.print(e.toString());
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
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
                    int albumChoice = scanner.nextInt();
                    switch (albumChoice){
                        case 0:
                            addCopy();
                            break;
                        default:
                            addAlbumCopy(albums.get(albumChoice - 1));
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
    void brokenCopy() {
        Menu.print("KAPOT EXEMPLAAR");
        Menu.printStripes();

        try{
            Menu.print("Is er een film of een album kapot gegaan? (F/A):");
            while (true) {
                String choice = scanner.next();
                if (choice.equals("F")) {
                    CallableStatement proc = c.prepareCall("{ call broken_movie_copy(?)}");
                    ArrayList<Integer> movies = searchMovie();
                    Menu.print("Toets 0 voor opnieuw zoeken");
                    int movieChoice = scanner.nextInt();
                    switch (movieChoice){
                        case 0:
                            brokenCopy();
                            return;
                        default:
                            proc.setInt(1, movies.get(movieChoice - 1));
                            proc.execute();
                            break;
                    }
                    proc.close();
                    break;
                } else if (choice.equals("A")) {
                    CallableStatement proc = c.prepareCall("{ call broken_album_copy(?)}");
                    ArrayList<Integer> albums = searchAlbum();
                    Menu.print("Toets 0 voor opnieuw zoeken");
                    int albumChoice = scanner.nextInt();
                    switch (albumChoice){
                        case 0:
                            brokenCopy();
                            return;
                        default:
                            proc.setInt(1, albums.get(albumChoice - 1));
                            proc.execute();
                            break;
                    }
                    proc.close();
                    break;
                } else {
                    Menu.print("Vul F(ilm) of A(lbum) in.");
                }
            }
        } catch (Exception e){
            Menu.print(e.toString());
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
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
            Menu.print(e.toString());
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

        try {
            searchPerson();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String name = scanner.next();
        // TODO: zoeken naar film en keuze menu laten zien?
        // TODO: Wat aanpassen?
    }

    /**
     * Adds a loan to the database
     */
    void addLoan() {
        Menu.print("UITLENEN");
        Menu.printStripes();
        Menu.print("Film/album naam:");
        String movieAlbum = scanner.nextLine().trim();
        Copy choseCopy = null ;
        Customer choseCustomer = null;
        try {
            List<Copy> copies = findloanableMedia(movieAlbum);
            if(copies != null && !copies.isEmpty())
            {
                Menu.print("ID \t\t type \t\t naam/titel \t\t uitgever \t\t uitgifte datum");
                for(int i = 0; i < copies.size(); i ++)
                {
                    Menu.print(i + "\t\t" + copies.get(i).toString());
                }
                Menu.print("Voor het id in");
                int choice =  scanner.nextInt();
                Menu.print("KEUZE " + choice);
                if(choice >=0 && choice < copies.size())
                {
                    choseCopy = copies.get(choice);
                }
                else
                {
                    Menu.print("Ongeldige keuze");
                    return;
                }
            }
            else
            {
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Customer> customers = searchCustomerUntilResults();
        if(customers != null &&  !customers.isEmpty() )
        {
            Menu.print("Voor het id in");
            int choice =  scanner.nextInt();
            for(Customer c : customers)
            {
                if(c.getId() == choice)
                {
                    choseCustomer  = c;
                    break;
                }
            }
            if(choseCustomer != null)
            {
                try {
                    Menu.print("Reseveren indien niet beschikbaar J/N");
                    boolean reseveNeeded = askYNQuestion();

                    Menu.print("Aantal dagen voer 0 in om de standaard waarde te gebruiken");
                    int numberOfDays = scanner.nextInt();
                    executeLoanResevationRequest(choseCustomer, choseCopy, reseveNeeded, numberOfDays);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    /**
     * Adds a return date to the loan. This method is mend for returning copies.
     */
    void returnLoan() {
        Menu.print("UITLENING TERUG BRENGEN");
        Menu.printStripes();
        List<Customer>  customers = searchCustomerUntilResults();
        int id =  askCustomerId();
        Customer chosenCustomer = null;
        for(Customer c : customers)
        {
            if(c.getId() == id)
            {
                chosenCustomer = c;
                break;
            }
        }
        if(chosenCustomer !=  null)
        {
            try {

                if(getLoanCustomer(chosenCustomer.getId()).size() >0)
                {
                    Menu.print("Voer de id in gescheiden door een ,");
                    scanner.nextLine();
                    ArrayList<Integer> loanIds = new ArrayList<Integer>();
                    String invoer = scanner.nextLine();
                    if(invoer.trim().isEmpty())
                    {
                        return;
                    }
                    String [] ar= invoer.split(",");
                    for(String s : ar){
                        loanIds.add(Integer.parseInt(s));
                    }
                    returnLoanItems(loanIds);
                }
                else
                {
                    Menu.print("Geen exemplaren");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void returnLoanItems(ArrayList<Integer> loanIds) throws  SQLException{
        if(loanIds != null)
        {
            for(int id : loanIds)
            {
                String query = "{  call return_loan(?)}";
                CallableStatement proc = c.prepareCall(query);
                proc.setInt(1 , id);
                if(proc.execute())
                {
                    Menu.print("SUCCESVOL TERUG GEBRACHT" + id);
                }
            }

            c.commit();
        }

    }

    /**
     * Retrieves the loan-history
     */
    void getHistory() {
        Menu.print("GESCHIEDENIS");
        Menu.printStripes();
        Menu.print("Voer title in:");
        String title = scanner.nextLine();

        try {
            List<Copy> copyList = searchCopy(title);
            if(copyList.isEmpty())
            {
                Menu.print("Geen exemplaren gevonden ");
                return;
            }
            Menu.print("ID \t\t type \t\t naam/titel \t\t uitgever \t\t uitgifte datum");
            for (Copy c : copyList)
            {
                Menu.print(c.getCopyId() + "\t\t"  + c.toString());
            }
            Menu.print("Voer een ID in");
            int copyId = scanner.nextInt();

            Menu.print("Aantal dagen terug");
            int numberOfDays = scanner.nextInt();
            getHistoryFromDB(copyId, numberOfDays);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getHistoryFromDB(int copyId , int numberOfDays) throws  SQLException{
        String query = "{call loan_history(?,?)}";
        CallableStatement proc = c.prepareCall(query);
        proc.setInt(1 , copyId);
        proc.setInt(2, numberOfDays);
        ResultSet rs = proc.executeQuery();
        Menu.print("Klant \t\t uitleen datum \t\t terugbreng datum }");
        while (rs.next())
        {
            Menu.print(rs.getString("customer_name") + "\t\t" + rs.getDate("start_date") + "\t\t" + rs.getDate("return_date") );
        }
    }

    /**
     * Searches an actor in the database
     */
    void searchActor() {
        Menu.print("ACTEUR ZOEKEN");
        Menu.printStripes();
        try {
            String query = "{call search_actor(?, ?)}";
            CallableStatement proc = c.prepareCall(query);
            Menu.print("Voornaam:");
            proc.setString(1, scanner.next());
            Menu.print("Achternaam:");
            proc.setString(2, scanner.next());

            ResultSet rs = proc.executeQuery();


            rs.close();
            proc.close();
        }  catch (Exception e){
            Menu.print(e.toString());
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
    }

    /**
     * Searches an artist in the database
     */
    void searchArtist() {
        Menu.print("ARTIEST ZOEKEN");
        Menu.printStripes();
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
    ArrayList<Integer> searchMovie() {
        Menu.print("FILM ZOEKEN");
        Menu.printStripes();
        ArrayList<Integer> albums = new ArrayList<Integer>();
        try{
            String query = "{call search_movie(?)}";
            CallableStatement st = c.prepareCall(query);
            Menu.print("Titel van film:");
            st.setString(1, scanner.nextLine());

            ResultSet rs = st.executeQuery();
            Menu.print("ID\t\t\tTitel\t\t\t\tUitgeef datum\t\tRegisseur\t\t\tUitgever\t\tGenre");
            int count = 1;
            while (rs.next())
            {
                albums.add(rs.getInt(1));
                Menu.print(rs.getInt(1) + "\t\t\t" + rs.getString(2) + "\t\t" + rs.getDate(3) + "\t\t\t" + rs.getString(4) + "\t\t" + rs.getString(5) + "\t\t" + rs.getString(6));
                CallableStatement st2 = c.prepareCall("{call get_artists_of_movie(?)}");
                st2.setInt(1, rs.getInt(1));
                ResultSet rs2 = st2.executeQuery();
                while (rs2.next())
                {
                    Menu.print("\t" + rs2.getString(1) + "\t\t" + rs2.getString(2) + "\t\t" + rs2.getDate(3));
                }
                Menu.printStripes();
                rs2.close();
                st2.close();
                count++;
            }
            rs.close();
            st.close();
            c.commit();
        } catch(Exception e){
            Menu.print(e.toString());
            Menu.print("Er is wat fout gegaan! Probeer het later nog eens.");
        }
        return albums;
    }

    /**
     * Searches an album in the database
     */
    ArrayList<Integer> searchAlbum() {
        Menu.print("ALBUM ZOEKEN");
        Menu.printStripes();
        ArrayList<Integer> albums = new ArrayList<Integer>();
        try {
            String query = "{call search_album(?)}";
            CallableStatement st = c.prepareCall(query);
            Menu.print("Naam van album:");
            st.setString(1, scanner.nextLine());

            ResultSet rs = st.executeQuery();
            Menu.print("ID\t\t\tName\t\tUitgeef datum\t\tUitgever\t\tArtiest");
            int count = 1;
            while (rs.next())
            {
                albums.add(rs.getInt(1));

                Menu.print(rs.getInt(1) + "\t\t\t" + rs.getString(2) + "\t\t" + rs.getDate(3) + "\t\t\t" + rs.getString(4) + "\t\t" + rs.getString(5));

                CallableStatement st2 = c.prepareCall("{call get_tracks_of_album(?)}");
                st2.setInt(1, rs.getInt(1));
                ResultSet rs2 = st2.executeQuery();
                int counter = 0;
                while (rs2.next())
                {
                    counter++;
                    Menu.print("\t" + counter + "\t" + rs2.getString(1));
                }
                Menu.printStripes();
                rs2.close();
                st2.close();
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
            String choice = scanner.next().toUpperCase();
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
            Menu.print(e.toString());
            // If it is not succesfull, the publisher already exists.
        }
        proc.close();
        return name;
    }

    private ArrayList<Integer> printTracks(ResultSet rs) throws SQLException{
        Menu.print("ID\t\t\tTitel\t\tTijdsduur\tArtiestnaam\t\t\tGenre\t\tAlbum_name\t\t\tAlbum_id");
        ArrayList<Integer> tracks = new ArrayList<Integer>();
        int count = 1;
        while (rs.next())
        {
            tracks.add(rs.getInt(1));

            Menu.print(rs.getInt(1) + "\t\t\t" + rs.getString(2) + "\t\t" + rs.getInt(3) + "\t\t\t" + rs.getString(4) + "\t\t" + rs.getString(5) + "\t\t" + rs.getString(6) + "\t\t" + rs.getInt(7));

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

    public List<Customer> searchCustomerUntilResults()
    {
        boolean hasResult = false;
        List<Customer> customers = null;
        while(!hasResult)
        {
            customers = searchCustomer();
            if(customers != null && !customers.isEmpty())
            {
                return customers;
            }
            else
            {
                Menu.print("Geen resultaten gevonden wilt u het nog maals proberen J/N");
                if(!askYNQuestion())
                {
                    break;
                }
            }
        }
        return  customers;
    }
    // Find movies and albums based on title
    public List<Copy> findloanableMedia(String title) throws  SQLException
    {
        List<Copy> copyList = new ArrayList<Copy>();
        String query = "{  call search_loanable(?)}";
        CallableStatement proc = c.prepareCall(query);
        Menu.print("title" + title);
        proc.setString(1, title);
        ResultSet rs = proc.executeQuery();
        while (rs.next())
        {
            copyList.add(new Copy(rs));
        }
        copyList.toString();
        return copyList;

    }
    public int askCustomerId()
    {
        Menu.print("Voer een klant id in");
        return scanner.nextInt();
    }
    public List<Loan> getLoanCustomer(int customerId) throws SQLException
    {
        List<Loan> loanList = new ArrayList<Loan>();
        String query = "{  call get_loan_customer(?)}";
        CallableStatement proc = c.prepareCall(query);
        proc.setInt(1,customerId);
        ResultSet rs =  proc.executeQuery();
        Menu.print("ID" + "\t\t" + "titel" + "\t\t" + "start datum" );
        while (rs.next())
        {
            Loan l = new Loan(rs);
            Menu.print(l.toString());
            loanList.add(l);
        }
        return  loanList;

    }

    public void executeLoanResevationRequest(Customer cust , Copy copy ,boolean reserveIfNotAvailable , int numberOfDays ) throws  SQLException
    {
        String query = "{ call new_loan_reservation(?, ?,? ,?,?)}";
        CallableStatement proc = c.prepareCall(query);
        proc.setInt(1, cust.getId());
        proc.setInt(2 ,  copy.getAlbumId());
        proc.setInt(3 , copy.getMovieId());
        proc.setInt(4 , numberOfDays);
        proc.setBoolean(5, reserveIfNotAvailable);
        //Menu.print(proc.toString());
        ResultSet rs = proc.executeQuery();
        c.commit();
        while (rs.next())
        {
            int loan_id = rs.getInt("loan_id");
            if(loan_id > 0)
            {
                Menu.print("Exemplaar uitgeleend");
                return;
            }
            int res_id = rs.getInt("reserved_id");
            if(res_id > 0)
            {
                Menu.print("Resevering gemaakt geen exemplaar meer beschikbaar voor uitlening");
            }

            Menu.print(rs.getInt("loan_id") + "");
        }

    }

    public List<Copy> searchCopy(String title ) throws  SQLException
    {
        List<Copy> copyList = new ArrayList<Copy>();
        String query = "{  call search_copy(?)}";
        CallableStatement proc = c.prepareCall(query);
        proc.setString(1, title);
        ResultSet rs = proc.executeQuery();
        while(rs.next())
        {
            copyList.add(new Copy(rs));
        }
        return  copyList;

    }

}
