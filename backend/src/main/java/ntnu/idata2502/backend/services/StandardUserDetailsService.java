package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Implementation of {@link UserDetailsService} for loading user-specific data.
 * This service is used by Spring Security for authentication and authorization.
 * It retrieves user details from the database and adapts them to Spring Security's {@link UserDetails}.
 */
@Service
public class StandardUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Constructs a new {@link StandardUserDetailsService}.
     *
     * @param userRepository the {@link UserRepository} used to access user data
     */
    public StandardUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user's details by their username.
     *
     * <p>This method is called by Spring Security during authentication to retrieve
     * user-specific data, such as username, password, and authorities.</p>
     *
     * <p>The user data is retrieved from the database using the {@link UserRepository},
     * and then mapped to a Spring Security {@link UserDetails} object. If the user
     * is not found, a {@link UsernameNotFoundException} is thrown.</p>
     *
     * @param username the username identifying the user whose data is required
     * @return a {@link UserDetails} object containing the user's data
     * @throws UsernameNotFoundException if the user cannot be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Attempt to retrieve the user from the database
        Optional<User> user = userRepository.findByUsername(username);

        // Throw an exception if the user is not found
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Convert the User entity to a Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(), // Username
                user.get().getPassword(), // Password
                new ArrayList<>() // Authorities (empty for now)
        );
    }
}
