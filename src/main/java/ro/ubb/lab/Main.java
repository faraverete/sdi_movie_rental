package ro.ubb.lab;

import jdk.nashorn.internal.ir.Assignment;
import org.xml.sax.SAXException;
import ro.ubb.lab.domain.Client;
import ro.ubb.lab.domain.Movie;
import ro.ubb.lab.domain.Rental;
import ro.ubb.lab.domain.validators.ClientValidator;
import ro.ubb.lab.domain.validators.MovieValidator;
import ro.ubb.lab.domain.validators.RentalValidator;
import ro.ubb.lab.repository.*;
import ro.ubb.lab.service.Service;
import ro.ubb.lab.ui.Console;

import java.sql.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by horatiu on 13.03.2017.
 */
public class Main {
    public static void workModeMenu()
    {
        String cmd = "Choose work mode:\n";
        cmd = cmd + "\t 0) In Memory Repository.\n";
        cmd = cmd + "\t 1) File Repository.\n";
        cmd = cmd + "\t 2) XML Repository.\n";
        cmd = cmd + "\t 3) DB Repository.\n";
        System.out.println(cmd);
    }


    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        MovieValidator movieValidator = new MovieValidator();
        ClientValidator clientValidator = new ClientValidator();
        RentalValidator rentalValidator = new RentalValidator();


        int cmd=0,ok=1;
        while(ok==1) {
            ok=0;
            workModeMenu();
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("Enter command:");
                cmd = Integer.parseInt(bufferRead.readLine());
                if(cmd<0 || cmd>3)
                    ok=1;
            } catch (NumberFormatException | IOException var5) {
                ok=1;
                System.out.println("Command should be an integer!");
            }
        }

        switch(cmd) {
            case 0:
                //init in-memory
                InMemoryRepository<Long, Movie> movieRepository = new InMemoryRepository<>(movieValidator);
                InMemoryRepository<Long, Client> clientRepository = new InMemoryRepository<>(clientValidator);
                InMemoryRepository<Long, Rental> rentalRepository = new InMemoryRepository<>(rentalValidator);
                Service movieService = new Service(movieRepository,clientRepository, rentalRepository);
                Console console = new Console(movieService);
                console.runConsole();
                break;
            case 1:
                //init file repos
                Repository<Long, Movie> movieFileRepo = new MovieFileRepo(movieValidator, "./movies");
                Repository<Long,Client> clientFileRepo=new ClientFileRepo(clientValidator,"./clients");
                Repository<Long,Rental> rentalFileRepo=new RentalFileRepo(rentalValidator,"rentals");
                Service movieFileService = new Service(movieFileRepo,clientFileRepo,rentalFileRepo);
                Console consoleFile = new Console(movieFileService);
                consoleFile.runConsole();
                break;
            case 2:
                //init xml repos
                Repository<Long, Movie> movieXMLrepo = new MovieXMLRepo(movieValidator, "./movies.xml");
                Repository<Long, Client> clientXMLrepo = new ClientXMLRepo(clientValidator, "./clients.xml");
                Repository<Long, Rental> rentalXMLrepo = new RentalXMLRepo(rentalValidator, "./rentals.xml");
                Service xmlservice = new Service(movieXMLrepo, clientXMLrepo, rentalXMLrepo);
                Console xmlconsole = new Console(xmlservice);
                xmlconsole.runConsole();
                break;

            case 3:
                //init db repos

                String url = "jdbc:postgresql://localhost:5432/movieRental";

                Repository<Long, Movie> movieDBRepository =
                        new MovieDBRepository(movieValidator, url,"postgres", "postgres");
                Repository<Long, Client> clientDBRepository =
                        new ClientDBRepository(clientValidator, url, "postgres", "postgres");
                Repository<Long, Rental> rentalDBRepository =
                        new RentalDBRepository(rentalValidator, url, "postgres", "postgres");

                Service service = new Service(movieDBRepository, clientDBRepository, rentalDBRepository);
                Console dbconsole = new Console(service);
                dbconsole.runConsole();
                break;
            default:
                System.out.println("Invalid command.");

        }
    }
}