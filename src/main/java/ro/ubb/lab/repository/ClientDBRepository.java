package ro.ubb.lab.repository;

import ro.ubb.lab.domain.Client;
import ro.ubb.lab.domain.validators.CustomException;
import ro.ubb.lab.domain.validators.Validator;
import ro.ubb.lab.domain.validators.ValidatorException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by horatiu on 04.04.2017.
 */
public class ClientDBRepository implements Repository<Long, Client> {
    private Validator<Client> validator;
    private String url;
    private String username;
    private String password;


    public ClientDBRepository(Validator<Client> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Client> findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null!");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from \"movieRental\".client where id=?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long clientId = resultSet.getLong("id");
                    String name = resultSet.getString("name");

                    Client client = new Client(name);
                    client.setId(clientId);

                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            throw new CustomException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from \"movieRental\".client");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String description = resultSet.getString("name");

                Client client = new Client(description);
                client.setId(id);

                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Optional<Client> save(Client var1) throws ValidatorException {
        if (var1 == null) {
            throw new IllegalArgumentException("entity must not be null");
        }

        validator.validate(var1);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "insert into \"movieRental\".client (name) values (?)")) {
            statement.setString(1, var1.getName());

            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(var1);
        }
    }

    @Override
    public Optional<Client> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        Optional<Client> client = findOne(id);

        if (!client.isPresent()) {
            return Optional.empty();
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "delete from \"movieRental\".client WHERE id=?")) {
            statement.setLong(1, id);

            statement.executeUpdate();

            return client;
        } catch (SQLException e) {
            throw new CustomException(e);
        }
    }

    @Override
    public Optional<Client> update(Client var1) throws ValidatorException {
        if (var1 == null) {
            throw new IllegalArgumentException("entity must not be null");
        }

        validator.validate(var1);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "update \"movieRental\".client set name=? where id=?")) {
            statement.setString(1, var1.getName());
            statement.setLong(2, var1.getId());

            statement.executeUpdate();

            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(var1);
        }
    }
}

