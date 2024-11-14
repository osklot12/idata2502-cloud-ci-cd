package ntnu.idata2502.backend.util;

import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Utility class for authentication.
 */
public class AuthUtil {
    private static UserRepository userRepository;

    /**
     * Injects UserRepository.
     *
     * @param userRepository the user repository to inject
     */
    public static void setUserRepository(UserRepository userRepository) {
        AuthUtil.userRepository = userRepository;
    }

    /**
     * Gets the current authenticated user.
     *
     * @return the current user
     */
    public static User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
