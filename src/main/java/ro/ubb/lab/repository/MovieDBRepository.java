package ro.ubb.lab.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ro.ubb.lab.domain.Movie;
import ro.ubb.lab.domain.validators.CustomException;
import ro.ubb.lab.domain.validators.Validator;
import ro.ubb.lab.domain.validators.ValidatorException;

/**
 * Created by horatiu on 04.04.2017.
 */

public class MovieDBRepository implements Repository<Long, Movie>
{
    private Validator<Movie> validator;
    private String url;
    private String username;
    private String password;


    public MovieDBRepository(Validator<Movie> movieValidator, String url, String username, String password)
    {
        this.validator = movieValidator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Movie> findOne(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id must not be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from \"movieRental\".movie where id=?"))
        {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery())
            {
                if (resultSet.next())
                {
                    Long movieId = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String genre = resultSet.getString("genre");

                    Movie movie = new Movie(name, genre);
                    movie.setId(movieId);
                    return Optional.of(movie);
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
    public Iterable<Movie> findAll()
    {
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from \"movieRental\".movie"))
        {
            try (ResultSet resultSet = statement.executeQuery())
            {
                while (resultSet.next())
                {
                    Long movieId = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String genre = resultSet.getString("genre");

                    Movie movie = new Movie(name,genre);
                    movie.setId(movieId);

                    movies.add(movie);
                }
            }
        }
        catch (SQLException e)
        {
            throw new CustomException(e);
        }

        return movies;
    }

    @Override
    public Optional<Movie> save(Movie entity) throws ValidatorException
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("entity must not be null");
        }

        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "insert into \"movieRental\".movie (name, genre) values (?,?)"))
        {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getGenre());

            statement.executeUpdate();

            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Movie> delete(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id must not be null");
        }
        Optional<Movie> movie = findOne(id);
        if (!movie.isPresent())
        {
            return Optional.empty();
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM \"movieRental\".movie WHERE id=?"))
        {
            statement.setLong(1, id);

            statement.executeUpdate();

            return movie;
        }
        catch (SQLException e)
        {
            throw new CustomException(e);
        }
    }

    @Override
    public Optional<Movie> update(Movie entity) throws ValidatorException
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("entity must not be null");
        }
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "update \"movieRental\".movie set name=?,genre=? WHERE id=?"))
        {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getGenre());
            statement.setLong(3, entity.getId());

            statement.executeUpdate();

            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.of(entity);
        }
    }
}
