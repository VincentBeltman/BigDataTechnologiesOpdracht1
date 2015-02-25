package nl.saxion.bd.opdracht1;

import java.sql.Date;
import java.sql.ResultSet;

import java.sql.SQLException;

/**
 * Created by Mike on 19-2-2015.
 */
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String city;
    private String address;
    private String zipcode;
    private String housenumber;

    public Customer(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.firstName = resultSet.getString("first_name");
        this.lastName = resultSet.getString("last_name");
        this.email = resultSet.getString("email");
        this.city = resultSet.getString("city");
        this.address = resultSet.getString("address");
        this.zipcode = resultSet.getString("zipcode");
        this.housenumber =resultSet.getString("housenumber");
        this.dateOfBirth = resultSet.getDate("date_of_birth");



    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    @Override
    public String toString() {
        return id + "\t\t\t" +  firstName + "\t\t" + lastName +"\t\t" + dateOfBirth.toString()
                + "\t\t" + email + "\t\t" + address + "\t\t" + housenumber + "\t\t"
                + zipcode + "\t\t" + city;

    }
}
