package com.Project.UserService.Utilities;

import com.Project.UserService.Database.Client;
import com.Project.UserService.Database.ClientsRepository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.regex.Pattern;

public class ClientValidator {

    private static final String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    private static final int MIN_AGE = 18;


    public static void validateSignUp(String firstName, String lastName, Integer age, String email, String pswd, ClientsRepository clientsRepository) throws IllegalArgumentException {

        if (firstName == null || lastName == null || firstName.equals("") || lastName.equals(""))
            throw new IllegalArgumentException("Invalid name!");

        if (age == null || age < MIN_AGE)
            throw new IllegalArgumentException("Invalid age!");

        if (email == null || email.equals(""))
            throw new IllegalArgumentException("Invalid email!");

        Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

        if (!pattern.matcher(email).matches())
            throw new IllegalArgumentException("Invalid email!");

        if (clientsRepository.findByEmail(email).isPresent())
            throw new IllegalArgumentException("Email already in use!");

        if (pswd == null || pswd.length() < 8)
            throw new IllegalArgumentException("Invalid password!");

    }

    public static Client validateLogIn(String email, String pswd, ClientsRepository clientsRepository) throws IllegalStateException {

        if (email == null || email.equals(""))
            throw new IllegalArgumentException("Invalid email!");

        if (pswd == null || pswd.length() < 8)
            throw new IllegalArgumentException("Invalid password!");

        Optional<Client> client = clientsRepository.findByEmail(email);

        try {

            if (!client.isPresent() || !Password.check(pswd, client.get().getPassword()))
                throw new IllegalArgumentException("Wrong Email/password!");

        } catch (Exception e) {

            throw new IllegalArgumentException("Wrong Email/password!");

        }

        return client.get();
    }
}
