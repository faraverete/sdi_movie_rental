package ro.ubb.lab.domain.validators;

import ro.ubb.lab.domain.Movie;

/**
 * Created by horatiu on 13.03.2017.
 */
public class MovieValidator implements Validator<Movie> {
    @Override
    public void validate(Movie entity) throws ValidatorException
    {
        String err = "";

        Long id = entity.getId();
        String name = entity.getName();
        String genre = entity.getGenre();
        if (id == null){
            err += "ID cannot be null ";

        }

        if (name == null){
            err += "Name cannot be null ";

        }
        if (genre == null){
        err += "Genre cannot be null ";

        }
        if (id < 0){
            err += "ID must be > 0 ";
        }

        if (name.equals("")){
            err += "Name must be non-empty ";
        }

        if (genre.equals("")){
            err += "Genre must be non-empty ";
        }

        if (!err.equals(""))
        {
            throw new ValidatorException(err);
        }


    }
}
