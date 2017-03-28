package ro.ubb.lab.repository;

import ro.ubb.lab.domain.Rental;
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
public class RentalFileRepo extends InMemoryRepository<Long,Rental> {
    private String filename;
    public RentalFileRepo(Validator<Rental> validator, String _filename) {
        super(validator);
        filename = _filename;
        loadData();
    }

    private void loadData() {
        Path path = Paths.get(filename);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                //Long id = Long.valueOf(items.get(0));
                Long movieID = Long.valueOf(items.get(0));
                Long clientID = Long.valueOf(items.get(1));
                Rental rental = new Rental(movieID,clientID);

                try {
                    super.save(rental);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Rental> save(Rental entity) throws ValidatorException {
        Optional<Rental> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    private void saveToFile(Rental entity) {
        Path path = Paths.get(filename);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.newLine();
            bufferedWriter.write(entity.getMovieID()+","+entity.getClientID());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Rental> update(Rental entity) throws ValidatorException {
        Optional<Rental> optional = super.update(entity);
        saveAllToFile();
        if (optional.isPresent()) {
            return optional;
        }
        return Optional.empty();
    }

    private void saveAllToFile() {
        Path path = Paths.get(filename);
        Iterable<Rental> assignments=super.findAll();
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            for(Rental entity:assignments){

                bufferedWriter.write(
                        entity.getMovieID() + "," + entity.getClientID());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
