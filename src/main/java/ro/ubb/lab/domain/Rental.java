package ro.ubb.lab.domain;

import ro.ubb.lab.utils.IdGenerator;

import java.util.Date;
import java.util.Objects;

/**
 * Created by horatiu on 27.03.2017.
 */
public class Rental extends BaseEntity<Long> {
    private Long movieID;
    private Long clientID;


    public Long getMovieID() {
        return movieID;
    }

    public Long getClientID() {
        return clientID;
    }



    public Rental(Long movieID, Long clientID)
    {
        this.setId(IdGenerator.generateID());
        this.movieID = movieID;
        this.clientID = clientID;


    }




    public Rental(Long id, Long movieID, Long clientID)
    {
        super.setId(id);
        this.movieID = movieID;
        this.clientID = clientID;

    }


    public void setMovieID(Long movieID) {
        this.movieID = movieID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }



    @Override
    public String toString() {
        return "Rental{" +
                "movieID=" + movieID +
                ", clientID=" + clientID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return Objects.equals(movieID, rental.movieID) &&
                Objects.equals(clientID, rental.clientID);

    }

    @Override
    public int hashCode() {
        return Objects.hash(movieID, clientID);
    }
}