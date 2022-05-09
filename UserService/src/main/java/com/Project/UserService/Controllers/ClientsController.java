package com.Project.UserService.Controllers;

import com.Project.UserService.Utilities.ClientValidator;
import com.Project.UserService.Database.Client;
import com.Project.UserService.Database.ClientsRepository;
import com.Project.UserService.Utilities.Password;
import com.Project.UserService.Utilities.ResourceNotFoundException;
import com.sun.istack.logging.Logger;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;

/**
 * The type Clients controller.
 */
@RestController
@RequestMapping(path="/clients")
public class ClientsController {

    @Autowired
    private ClientsRepository clientsRepository;

    private final HashMap<String, Client> loggedUsers = new HashMap<>();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Gets all clients.
     *
     * @return the all clients
     */
    @GetMapping(path = "/get")
    @Timed(value = "getAllClients.time", description = "Time taken to find all the clients.")
    public @ResponseBody
    String getAllClients() {

        List<Client> auxList = clientsRepository.findAll();

        return new Gson().toJson(auxList);

    }

    /**
     * Gets client by id.
     *
     * @param clientId the client id
     * @return the client by id
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/id/{id}")
    public @ResponseBody
    String getClientById(@PathVariable(value = "id") Integer clientId) throws ResourceNotFoundException {

         Client client = clientsRepository.findById(clientId).orElseThrow(() ->
                 new ResourceNotFoundException("Client not found with id :" + clientId));

        return new Gson().toJson(ResponseEntity.ok().body(client));

    }

    /**
     * Gets client by email.
     *
     * @param clientEmail the client email
     * @return the client by email
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/email/{email}")
    public @ResponseBody
    String getClientByEmail(@PathVariable(value = "email") String clientEmail) throws ResourceNotFoundException {

        Client client = clientsRepository.findByEmail(clientEmail).orElseThrow(() ->
                new ResourceNotFoundException("Client not found with id :" + clientEmail));

        return new Gson().toJson(ResponseEntity.ok().body(client));

    }

    /**
     * Sign up string.
     *
     * @param age       the age
     * @param lastName  the last name
     * @param firstName the first name
     * @param email     the email
     * @param pswd      the pswd
     * @return the string
     */
    @GetMapping("/signup")
    @Timed(value = "signUp.time", description = "Time taken to sign up an user")
    public @ResponseBody
    String signUp(@RequestParam(name = "age", required = false) Integer age,
                  @RequestParam(name = "lastName", required = false) String lastName,
                  @RequestParam(name = "firstName", required = false) String firstName,
                  @RequestParam(name = "email", required = false) String email,
                  @RequestParam(name = "pswd", required = false) String pswd) {

        try {
            ClientValidator.validateSignUp(firstName, lastName, age, email, pswd, clientsRepository);
        } catch (Exception e) {
            return new Gson().toJson(e.getMessage());
        }

        try {
            String hashed = Password.getSaltedHash(pswd);
            clientsRepository.insertClient(firstName, lastName, age, email, hashed, false, false);
        } catch (Exception e) {
            return new Gson().toJson(e.getMessage());
        }

        logger.info("The user with email " + email + " just created an account!");

        return new Gson().toJson("Success!");

    }

    /**
     * Login string.
     *
     * @param email     the email
     * @param pswd      the pswd
     * @param sessionId the session id
     * @return the string
     * @throws IllegalStateException the illegal state exception
     */
    @GetMapping("/login")
    public @ResponseBody
    String login(@RequestParam(name = "email") String email,
                 @RequestParam(name = "pswd") String pswd,
                 @RequestParam(name = "sessionId", required = false) String sessionId) throws IllegalStateException {

        Client client;
        try {
            client = ClientValidator.validateLogIn(email, pswd, clientsRepository);
        } catch (IllegalArgumentException e) {
            return new Gson().toJson(ResponseEntity.ok().body(new Client(false, e.getMessage())));
        }

        System.out.println("The user with email " + email + " just logged in!");

        loggedUsers.put(sessionId, client);
        return new Gson().toJson(ResponseEntity.ok().body(client));

    }

    /**
     * Gets client session.
     *
     * @param sessionId the session id
     * @return the client session
     */
    @GetMapping("/session")
    public @ResponseBody
    String getClientSession(@RequestParam(name = "id") String sessionId) {

        return new Gson().toJson(ResponseEntity.ok()
                .body(loggedUsers.getOrDefault(sessionId, new Client(false, ""))));

    }

    /**
     * Logout string.
     *
     * @param sessionId the session id
     * @return the string
     */
    @GetMapping("/logout")
    public @ResponseBody
    String logout(@RequestParam(name = "sessionId") String sessionId) {

        return new Gson().toJson(ResponseEntity.ok().body(loggedUsers.remove(sessionId)));

    }

    /**
     * Gets logged users.
     *
     * @return the logged users
     */
    public HashMap<String, Client> getLoggedUsers() {

        return loggedUsers;

    }

}