package nl.saxion.bd.opdracht1;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Mike on 27-2-2015.
 */
public class Loan {
    private  int id;
    private  String title;
    private Date start_date;

    public  Loan (ResultSet rs) throws SQLException
    {
        this.id = rs.getInt("loan_id");
        this.title =  rs.getString("title");
        this.start_date = rs.getDate("start_date");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return start_date;
    }

    public void setDate(Date date) {
        this.start_date = date;
    }
    @Override
    public String toString()
    {
        return  id + "\t\t" + title + "\t\t" + start_date;
    }
}
