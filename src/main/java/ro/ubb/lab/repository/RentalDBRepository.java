package ro.ubb.lab.repository;

/**
 * Created by horatiu on 04.04.2017.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ro.ubb.lab.domain.Rental;
import ro.ubb.lab.domain.validators.CustomException;
import ro.ubb.lab.domain.validators.Validator;
import ro.ubb.lab.domain.validators.ValidatorException;


public class RentalDBRepository implements Repository<Long, Rental>
{
    private Validator<Rental> validator;
    private String url;
    private String username;
    private String password;


    public RentalDBRepository(Validator<Rental> validator, String url, String username, String password)
    {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Rental> findOne(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id must not be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from \"movieRental\".rental where id=?"))
        {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next())
                {
                    Long rentalId = resultSet.getLong("id");
                    Long movieId = resultSet.getLong("movieId");
                    Long clientId = resultSet.getLong("clientId");

                    Rental rental = new Rental(rentalId, movieId, clientId);
                    return Optional.of(rental);
                }
            }
        }
        catch (SQLException e)
        {
            throw new CustomException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Rental> findAll()
    {
        List<Rental> rentals = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from \"movieRental\".rental");
             ResultSet resultSet = statement.executeQuery())
        {

            while (resultSet.next())
            {
                Long rentalId = resultSet.getLong("id");
                Long movieId = resultSet.getLong("movieId");
                Long clientId = resultSet.getLong("clientId");

                Rental rental = new Rental(rentalId, movieId, clientId);

                rentals.add(rental);
            }

        }
        catch (SQLException e)
        {
            throw new CustomException(e);
        }

        return rentals;
    }

    @Override
    public Optional<Rental> save(Rental var1) throws ValidatorException
    {
        if (var1 == null)
        {
            throw new IllegalArgumentException("entity must not be null");
        }

        validator.validate(var1);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "insert into \"movieRental\".rental (movieId, clientId) values (?,?)"))
        {
            statement.setLong(1, var1.getMovieID());
            statement.setLong(2, var1.getClientID());


            statement.executeUpdate();

            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.of(var1);
        }
    }

    @Override
    public Optional<Rental> delete(Long id)
    {
        if(id == null)
        {
            throw new IllegalArgumentException("ID must not be null!");
        }

        Optional<Rental> rental = findOne(id);

        if(!rental.isPresent())
        {
            return Optional.empty();
        }

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("delete from \"movieRental\".rental where id=?"))
        {
            statement.setLong(1, id);
            statement.executeUpdate();
            return rental;
        }
        catch (SQLException e)
        {
            throw new CustomException(e);
        }
    }

    @Override
    public Optional<Rental> update(Rental var1) throws ValidatorException
    {
        if(var1 == null)
        {
            throw new IllegalArgumentException("Rental must not be null!");
        }

        validator.validate(var1);

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("update \"movieRental\".rental set movie_id=?, client_id=? where id=?"))
        {
            statement.setLong(1, var1.getMovieID());
            statement.setLong(2, var1.getClientID());
            statement.setLong(4, var1.getId());

            statement.executeUpdate();

            return Optional.empty();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return Optional.of(var1);
        }
    }
}

