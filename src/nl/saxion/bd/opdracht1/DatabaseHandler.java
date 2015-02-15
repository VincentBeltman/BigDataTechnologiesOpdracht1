package nl.saxion.bd.opdracht1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handler for the database.
 * The methods of this class will call the right procedure of the database.
 * Created by Vincent on 13-2-2015.
 */
public class DatabaseHandler {
    private static DatabaseHandler instance;
    protected DatabaseHandler() {

    }
    private Connection c;

    public static DatabaseHandler getInstance(){
        if(instance ==null){
            instance = new DatabaseHandler();
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

    }

    /**
     * Searches for a customer in the database
     */
    void searchCustomer() {

    }

    /**
     * Updates a customer in the database
     */
    void updateCustomer() {

    }

    /**
     * Adds an album to the database
     */
    void addAlbum() {

    }

    /**
     * Adds a movie to the databsae
     */
    void addMovie() {

    }

    /**
     * Adds a copy to the database
     */
    void addCopy() {

    }

    /**
     * Updates a copy in the database
     */
    void updateCopy() {

    }

    /**
     * Adds an actor to the database
     */
    void addActor() {

    }

    /**
     * Updates an actor in the database
     */
    void updateActor() {

    }

    /**
     * Adds an reservation to the database
     */
    void addReservation() {

    }

    /**
     * Adds a loan to the database
     */
    void addLoan() {

    }

    /**
     * Adds a return date to the loan. This method is mend for returning copies.
     */
    void returnLoan() {

    }

    /**
     * Retrieves the loan-history
     */
    void getHistory() {

    }

    /**
     * Searches an actor in the database
     */
    void searchActor() {

    }

    /**
     * Searches an artist in the database
     */
    void searchArtist() {

    }

    /**
     * Searches a movie in the database
     */
    void searchMovie() {

    }

    /**
     * Searches a track in the database
     */
    void searchTrack() {

    }

    /**
     * Searches an album in the database
     */
    void searchAlbum() {

    }
}
