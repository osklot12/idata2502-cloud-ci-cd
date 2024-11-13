package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.dto.RegisterRequest;
import ntnu.idata2502.backend.entities.User;

import java.util.Optional;

public interface UserService {
    /**
     * Registers a new user if the username is not already taken.
     *
     * @param regRequest the registration request
     * @return the newly created user or null if username is taken
     */
    public User registerUser(RegisterRequest regRequest);

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an Optional containing the user if found, or empty otherwise
     */
    public Optional<User> findUserByUsername(String username);
}
