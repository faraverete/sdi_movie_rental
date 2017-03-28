package ro.ubb.lab.domain.validators;

import ro.ubb.lab.domain.Rental;

import java.util.Date;

/**
 * Created by horatiu on 27.03.2017.
 */
public class RentalValidator implements Validator<Rental> {

    public RentalValidator(){

    }

    @Override

    public void validate(Rental rental) throws ValidatorException
    {
        String err = "";
        Long id = rental.getId();
        Long movieID = rental.getMovieID();
        Long clientID = rental.getClientID();


        if(id == null)
        {
            err += "ID must not be null. ";
        }

        if(movieID == null)
        {
            err += "Student ID must not be null. ";
        }

        if(clientID == null)
        {
            err += "Problem ID must not be null";
        }

        if(id != null && id < 0)
        {
            err += "ID must be positive. ";
        }

        if(movieID != null && movieID < 0)
        {
            err += "Student ID must be positive. ";
        }

        if(clientID != null && clientID < 0)
        {
            err += "Problem ID must be positive. ";
        }


        if(!err.equals(""))
        {
            throw new ValidatorException(err);
        }
    }
}
