package nl.saxion.bd.opdracht1;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vincent on 19-2-2015.
 */
public class Person {
    private int ID;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;

    public Person(ResultSet rs) throws SQLException{
        ID = rs.getInt(1);
        firstName = rs.getString(2);
        lastName = rs.getString(3);
        dateOfBirth = rs.getDate(4);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return ID + "\t\t\t" + firstName + "\t\t" + lastName +"\t\t" + dateOfBirth;
    }
}
