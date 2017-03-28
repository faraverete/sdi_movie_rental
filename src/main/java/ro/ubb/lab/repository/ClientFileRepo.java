package ro.ubb.lab.repository;

import ro.ubb.lab.domain.Client;
import ro.ubb.lab.domain.validators.Validator;
import ro.ubb.lab.domain.validators.ValidatorException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by horatiu on 27.03.2017.
 */
public class ClientFileRepo extends InMemoryRepository<Long,Client> {
    private String filename;

    public ClientFileRepo(Validator<Client> validator, String _filename) {
        super(validator);
        filename = _filename;
        loadData();
    }

    private void loadData() {
        Path path = Paths.get(filename);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long id = Long.valueOf(items.get(0));
                String name = items.get(1);

                Client client = new Client(name);
                client.setId(id);

                try {
                    super.save(client);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        Optional<Client> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    private void saveToFile(Client entity) {
        Path path = Paths.get(filename);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.newLine();
            bufferedWriter.write(entity.getId() + "," + entity.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Client> delete(Long id) throws IllegalArgumentException {
        Optional<Client> optional = super.delete(id);
        saveAllToFile();
        return optional;
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException {
        Optional<Client> optional = super.update(entity);
        saveAllToFile();
        return optional;
    }

    private void saveAllToFile() {
        Path path = Paths.get(filename);
        Iterable<Client> problems=super.findAll();
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            for(Client entity:problems){

                bufferedWriter.write(
                        entity.getId() + "," + entity.getName());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
