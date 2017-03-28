package ro.ubb.lab.repository;

import ro.ubb.lab.domain.Movie;
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
public class MovieFileRepo extends InMemoryRepository<Long,Movie> {
    private String filename;

    public MovieFileRepo(Validator<Movie> validator, String _filename) {
        super(validator);
        filename = _filename;
        loadData();

    }

    /**
     * Loads data from file {@code filename}.
     *Builds the path to file, sparses each line and constructs the entities(Student/LabProblem/Assignment)
     * Calls the parent's save method for each new entity.
     */
    private void loadData() {
        Path path = Paths.get(filename);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long id = Long.valueOf(items.get(0));
                String name = items.get(1);
                String genre = items.get((2));

                Movie movie = new Movie(name, genre);
                movie.setId(id);

                try {
                    super.save(movie);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public Optional<Movie> save(Movie entity) throws ValidatorException {
        Optional<Movie> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    /**
     *
     * @param entity
     * Writes to file the string corresponding to each new entity added in APPEND MODE.
     * Catches any IOExceptions that may occur during file opening.
     */
    private void saveToFile(Movie entity) {
        Path path = Paths.get(filename);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.newLine();
            bufferedWriter.write(
                    entity.getId() + "," + entity.getName() + "," + entity.getGenre());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Movie> delete(Long id) throws IllegalArgumentException {
        Optional<Movie> optional = super.delete(id);
        saveAllToFile();
        return optional;
    }

    @Override
    public Optional<Movie> update(Movie entity) throws ValidatorException {
        Optional<Movie> optional = super.update(entity);
        saveAllToFile();
        return optional;
    }


    /**
     *
     * File is opened in TRUNCATE_EXISTING mode.
     * Adds to file all records after a delete or an update operation was executed on records.
     * Catches all IOExceptions that may occur in file opening.
     */
    private void saveAllToFile() {
        Path path = Paths.get(filename);
        Iterable<Movie> movies=super.findAll();
        //Set allStudents = StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
        //Stream studStream=allStudents.stream();
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            for(Movie entity:movies){

                bufferedWriter.write(
                        entity.getId() + ","  + entity.getName() + "," + entity.getGenre());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
