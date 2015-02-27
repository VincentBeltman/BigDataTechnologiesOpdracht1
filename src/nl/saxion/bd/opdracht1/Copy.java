package nl.saxion.bd.opdracht1;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

/**
 * Created by Mike on 27-2-2015.
 */
public class Copy {
    private int albumId;
    private int movieId;
    private String title , publisher;
    private Date releaseDate;

    public Copy(ResultSet rs) throws SQLException {

        albumId = rs.getInt("album_id");
        movieId = rs.getInt("movie_id");
        title = rs.getString("title");
        publisher = rs.getString("publisher");
        releaseDate = rs.getDate("release_date");


    }

    public int getAlbumId()
    {
        return albumId;
    }
    public  int getMovieId()
    {
        return  movieId;
    }

    public boolean isAlbum()
    {
        if(movieId == 0 && albumId > 0)
        {
            return  true;
        }
        return  false;
    }

    @Override
    public String toString()
    {
        return  (isAlbum() ? "Album: " : "Film:") + "\t\t" + title + "\t\t" + publisher +  "\t\t" + releaseDate;
    }
}



