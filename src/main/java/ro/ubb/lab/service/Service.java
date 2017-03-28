package ro.ubb.lab.service;


import ro.ubb.lab.domain.Client;
import ro.ubb.lab.domain.Movie;
import ro.ubb.lab.domain.Rental;
import ro.ubb.lab.domain.validators.CustomException;
import ro.ubb.lab.domain.validators.ValidatorException;
import ro.ubb.lab.repository.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by horatiu on 28.03.2017.
 */
public class Service {
    private Repository<Long, Movie> movieRepo;
    private Repository<Long, Client> clientRepo;
    private Repository<Long, Rental> rentalRepo;

    public Service(
            Repository<Long, Movie> movieRepo,
            Repository<Long, Client> _clientRepo,
            Repository<Long, Rental> _rentalRepo) {
        this.movieRepo = movieRepo;
        clientRepo = _clientRepo;
        rentalRepo = _rentalRepo;
    }
    /**
     * Saves the given movie.
     *
     * @param movie
     *            must not be null.
     * @throws ValidatorException
     *             if the given student is not valid
     */
    public void addMovie(Movie movie) throws ValidatorException {
        this.movieRepo.save(movie);
    }

    /**
     * Get all movies from repository.
     * @return {@code Set<Student>}
     */
    public Set<Movie> getAllMovies() {
        Iterable<Movie> movies = this.movieRepo.findAll();
        return StreamSupport.stream(movies.spliterator(), false).collect(Collectors.toSet());
    }


    /**
     * Removes the entity with the given id.
     *
     * @param id
     *            must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws IllegalArgumentException
     *             if the given id is null.
     */
    public Optional<Movie> deleteMovie(Long id) throws IllegalArgumentException {
        Optional s = this.movieRepo.delete(id);
        return s;
    }

    /**
     * Updates the given entity.
     *
     * @param m {@code Movie}
     *            must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *         entity.
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidatorException
     *             if the entity is not valid.
     */
    public Optional<Movie> updateMovie(Movie m) {
        return this.movieRepo.update(m);
    }

    /**
     * Saves the given client.
     *
     * @param c
     *            must not be null.
     * @throws ValidatorException
     *             if the given student is not valid
     */
    public void addClient(Client c)
    {
        this.clientRepo.save(c);
    }

    /**
     * Get all clients from repository.
     * @return {@code Set<LabProblem>}
     */
    public Set<Client> getAllClients()
    {
        Iterable<Client> clients=this.clientRepo.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Removes the entity with the given id.
     *
     * @param id
     *            must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws IllegalArgumentException
     *             if the given id is null.
     */
    public Optional<Client> deleteClient(Long id)
    {
        return this.clientRepo.delete(id);
    }

    /**
     * Updates the given entity.
     *
     * @param c {@code LabProblem}
     *            must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *         entity.
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidatorException
     *             if the entity is not valid.
     */
    public Optional<Client> updateClient(Client c)
    {
        return this.clientRepo.update(c);
    }


    /**
     * Assign the given movie to the given client.
     * @param movieID {@code Long}
     *                  must not be null.
     * @param clientID {@code Long}
     *                  must not be null.
     */
    public void addRental(Long movieID, Long clientID)
    {
        if(!movieRepo.findOne(movieID).isPresent())
            throw new CustomException("No student with this ID!");

        if(!clientRepo.findOne(clientID).isPresent())
            throw new CustomException("No problem with this ID!");
        Rental rental = new Rental(movieID, clientID);
        rentalRepo.save(rental);

    }


    /**
     * Return all rentals from repository
     * @return {@code Set<Rentals>}
     */
    public Set<Rental> getAllRentals()
    {
        Iterable<Rental> rentals = this.rentalRepo.findAll();
        return StreamSupport.stream(rentals.spliterator(), false).collect(Collectors.toSet());
    }


    /**
     * Filters movies by their name.
     * @param m {@code String}
     * @return {@code Set<Movie>} - the set of students whose name contain the given string.
     */
    public Set<Movie> filterMoviesbyName(String m){
        Iterable<Movie> movies = movieRepo.findAll();
        //Version1
        Set<Movie> filteredMovies = StreamSupport.stream(movies.spliterator(), false).filter(student->student.getName().contains(m)).collect(Collectors.toSet());
        //Version2:
        //Set<Movie> filteredMovies = new HashSet<>();
        //movies.forEach(filteredmovies::add);
        //filteredMovies.removeIf(movie -> !movie.getName().contains(s));

        return filteredMovies;
    }


    public void reportByGenre() {
        Iterable<Movie> movies = movieRepo.findAll();
        Map<String, Long> res = StreamSupport.stream(movies.spliterator(), false)
                .collect(Collectors.groupingBy(Movie::getGenre, Collectors.counting()));
        System.out.println(res.toString());
    }

    /**
     * Report: the client renting most movies
     * @return {@code Client} - the client which is renting  most movies.
     */
    public Client clientRentingMostMovies()
    {
        Iterable<Rental> rentals = rentalRepo.findAll();
        Map<Long, Long> res = StreamSupport.stream(rentals.spliterator(), false)
                .collect(Collectors.groupingBy(Rental::getClientID, Collectors.counting()));
        //map: pbID->no. of clients

        Long idOfMax = res.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        return clientRepo.findOne(idOfMax).get();
    }


}