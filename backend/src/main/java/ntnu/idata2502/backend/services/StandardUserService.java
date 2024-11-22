package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.dto.RegisterRequest;
import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.exceptions.UserRegistrationException;
import ntnu.idata2502.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link UserService} that provides standard operations for user management.
 * This service handles user registration and retrieval operations, ensuring proper validation and
 * secure password handling.
 */
@Service
public class StandardUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for {@link StandardUserService}.
     *
     * @param userRepository  the {@link UserRepository} for accessing and persisting user data
     * @param passwordEncoder the {@link PasswordEncoder} for securely hashing passwords
     */
    @Autowired
    public StandardUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user with the provided registration details.
     *
     * <p>The method performs the following steps:
     * <ul>
     *     <li>Validates that the username and email are not already taken.</li>
     *     <li>Hashes the user's password using the provided {@link PasswordEncoder}.</li>
     *     <li>Creates a new {@link User} entity and persists it using the {@link UserRepository}.</li>
     * </ul>
     *
     * @param regRequest the {@link RegisterRequest} containing user registration details
     * @return the newly created {@link User}
     * @throws UserRegistrationException if the username or email already exists
     */
    @Override
    public User registerUser(RegisterRequest regRequest) {
        // Check if the username already exists
        if (userRepository.existsByUsername(regRequest.username())) {
            throw new UserRegistrationException("User with username " + regRequest.username() + " already exists");
        }

        // Check if the email already exists
        if (userRepository.existsByEmail(regRequest.email())) {
            throw new UserRegistrationException("User with email " + regRequest.email() + " already exists");
        }

        // Encode the password securely
        String encodedPassword = passwordEncoder.encode(regRequest.password());

        // Create a new user instance
        User newUser = new User(regRequest.username(), regRequest.email(), encodedPassword);

        // Persist the user in the database
        return userRepository.save(newUser);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an {@link Optional} containing the {@link User} if found, or empty if not
     */
    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
