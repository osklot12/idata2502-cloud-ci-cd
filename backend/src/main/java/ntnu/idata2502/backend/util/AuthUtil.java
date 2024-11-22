package ntnu.idata2502.backend.util;

import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Utility class for authentication-related operations.
 *
 * <p>This class provides methods for retrieving the currently authenticated user and
 * managing authentication dependencies. It acts as a bridge between the application's
 * security context and the persistence layer.</p>
 */
public class AuthUtil {

    /**
     * The repository for accessing user data. This is statically injected and
     * used to retrieve the authenticated user's details from the database.
     */
    private static UserRepository userRepository;

    /**
     * Injects the {@link UserRepository} for accessing user data.
     *
     * <p>This method allows the application to set a {@link UserRepository} instance
     * statically. This approach is used to enable {@code AuthUtil} to retrieve user
     * details without being a Spring-managed bean itself.</p>
     *
     * @param userRepository the {@link UserRepository} to inject
     */
    public static void setUserRepository(UserRepository userRepository) {
        AuthUtil.userRepository = userRepository;
    }

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * <p>This method performs the following steps:
     * <ul>
     *     <li>Gets the username of the authenticated user from the Spring Security context.</li>
     *     <li>Fetches the user's details from the {@link UserRepository}.</li>
     *     <li>Throws a {@link UsernameNotFoundException} if the user cannot be found in the database.</li>
     * </ul>
     * </p>
     *
     * @return the authenticated {@link User}
     * @throws UsernameNotFoundException if the authenticated user is not found in the database
     */
    public static User getCurrentUser() {
        // Retrieve the username of the currently authenticated user from the security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find the user by username and throw an exception if not found
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
