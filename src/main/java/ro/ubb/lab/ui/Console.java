package ro.ubb.lab.ui;

import ro.ubb.lab.domain.Client;
import ro.ubb.lab.domain.Rental;
import ro.ubb.lab.domain.validators.CustomException;
import ro.ubb.lab.domain.validators.ValidatorException;
import ro.ubb.lab.service.Service;
import ro.ubb.lab.domain.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by horatiu on 12.03.2017.
 */
public class Console {
    private Service service;

    public Console(Service service) {
        this.service = service;
    }

    public void runConsole() {
        boolean working = true;

        while(working) {
            this.printMenu();
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

            int cmd;
            try {
                System.out.println("Enter command:");
                cmd = Integer.parseInt(bufferRead.readLine());
            } catch (NumberFormatException | IOException var5) {
                System.out.println("Command should be an integer!");
                continue;
            }

            switch(cmd) {
                case 0:
                    working = false;
                    break;
                case 1:
                    this.addMovie();
                    break;
                case 2:
                    this.deleteMovie();
                    break;
                case 3:
                    this.updateMovie();
                    break;
                case 4:
                    this.printAllMovies();
                    break;
                case 5:
                    this.addClient();
                    break;
                case 6:
                    this.deleteClient();
                    break;
                case 7:
                    this.updateClient();
                    break;
                case 8:
                    this.getAllClients();
                    break;
                case 9:
                    this.addRental();
                    break;
                case 10:
                    this.printAllRentals();
                    break;
                case 11:
                    this.filterMoviesbyName();
                    break;
                case 12:
                    this.clientRentingMostMovies();
                    break;
                case 13:
                    this.filterMoviesByGenre();
                default:
                    System.out.println("Invalid command!");
            }
        }

    }


    private void printMenu() {
        String cmd = "Commands:\n";
        cmd = cmd + "\t 0) Exit.\n";
        cmd = cmd + "\t 1) Add a movie.\n";
        cmd = cmd + "\t 2) Delete a movie.\n";
        cmd = cmd + "\t 3) Update a movie.\n";
        cmd = cmd + "\t 4) Print all movies.\n";
        cmd = cmd + "\t 5) Add a client.\n";
        cmd = cmd + "\t 6) Delete a client.\n";
        cmd = cmd + "\t 7) Update a client.\n";
        cmd = cmd + "\t 8) Show all clients.\n";
        cmd = cmd + "\t 9) Add rental.\n";
        cmd = cmd + "\t 10) Show rentals.\n";
        cmd = cmd + "\t 11) Filter movies by name.\n";
        cmd = cmd + "\t 12) Find the client renting most movies.\n";
        cmd = cmd + "\t 13) Report by genre.\n";
        System.out.println(cmd);
    }

    private void printAllMovies() {
        Set students = this.service.getAllMovies();
        Stream var10000 = students.stream();
        PrintStream var10001 = System.out;
        System.out.getClass();
        var10000.forEach(var10001::println);
    }

    private void addMovie() {
        Movie movie = this.readMovie();
        if(movie != null) {
            try {
                this.service.addMovie(movie);
            } catch (IllegalArgumentException | ValidatorException var3) {
                System.out.println(var3.getMessage());
            }
        }

    }

    private Movie readMovie() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.println("Give the ID:");
            Long id = Long.valueOf(bufferRead.readLine());
            System.out.println("Give the name:");
            String name = bufferRead.readLine();
            System.out.println("Give the genre:");
            String genre = bufferRead.readLine();
            Movie movie = new Movie(name, genre);
            movie.setId(id);
            return movie;
        } catch (NumberFormatException | IOException var7) {
            System.out.println("Invalid input!");
            return null;
        }
    }


    private void deleteMovie() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        Long id = null;
        System.out.println("Give the ID:");

        try {
            id = Long.valueOf(bufferRead.readLine());
        } catch (NumberFormatException | IOException var4) {
            System.out.println("Invalid input!");
        }

        if(id != null) {
            this.service.deleteMovie(id);
        }

    }

    private void updateMovie() {
        Movie movie = this.readMovie();
        if(movie != null) {
            try {
                this.service.updateMovie(movie);
            } catch (IllegalArgumentException | ValidatorException var3) {
                System.out.println(var3.getMessage());
            }
        }

    }

    private Client readClient()
    {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Give the ID:");
            Long id = Long.valueOf(bufferRead.readLine());
            System.out.println("Give the name:");
            String name = bufferRead.readLine();
            Client client=new Client(name);
            client.setId(id);
            return client;
        }catch(IOException | NumberFormatException e)
        {
            System.out.println("Invalid input!");
            return null;
        }
    }
    private void addClient()
    {
        Client labProb=readClient();
        if(labProb!=null)
        {
            try
            {
                this.service.addClient(labProb);

            }catch(IllegalArgumentException | ValidatorException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private void getAllClients()
    {
        Set clients = this.service.getAllClients();
        Stream allClients=clients.stream();
        PrintStream ps = System.out;
        System.out.getClass();
        allClients.forEach(ps::println);
    }

    private void deleteClient()
    {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        Long id = null;
        System.out.println("Give the ID:");
        try {
            id = Long.valueOf(bufferRead.readLine());
        } catch (NumberFormatException | IOException var4) {
            System.out.println("Invalid input!");
        }

        if(id != null) {
            this.service.deleteClient(id);
        }
    }

    private void updateClient()
    {
        Client client=readClient();
        if(client!=null)
        {
            try {
                this.service.updateClient(client);

            }catch(IllegalArgumentException | ValidatorException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    //Alexandra
    private void addRental()
    {
        Long movieID = readID("Enter the ID of the movie: ");
        Long clientID = readID("Enter the ID of the client: ");

        if(movieID != null && clientID != null)
        {
            try
            {
                service.addRental(movieID, clientID);
            }
            catch(IllegalArgumentException | CustomException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }


    private Long readID(String message)
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(message);
        Long id = null;

        try
        {
            id = Long.valueOf(bufferedReader.readLine());
        }
        catch(NumberFormatException | IOException ex)
        {
            System.out.println("ID must be an integer!");
        }

        return id;
    }

    private void printAllRentals()
    {
        Set<Rental> idsOfRentals = service.getAllRentals();
        idsOfRentals.stream().forEach(System.out::println);
    }


    private void filterMoviesbyName()
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            System.out.println("Give the name: ");
            String name = bufferedReader.readLine();
            Set<Movie> filteredStudents = service.filterMoviesbyName(name);
            filteredStudents.stream().forEach(System.out::println);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

    }


    private void clientRentingMostMovies()
    {
        System.out.println(service.clientRentingMostMovies());
    }

    public void filterMoviesByGenre() {
        service.reportByGenre();
    }

}

