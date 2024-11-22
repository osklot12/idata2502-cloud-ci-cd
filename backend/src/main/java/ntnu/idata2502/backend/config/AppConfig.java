package ntnu.idata2502.backend.config;

import ntnu.idata2502.backend.repositories.UserRepository;
import ntnu.idata2502.backend.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Application-level configuration class for setting up dependencies.
 * Configures static utility classes with the required beans from the Spring context.
 */
@Configuration
public class AppConfig {

    /**
     * Injects the {@link UserRepository} bean into the {@link AuthUtil} utility class.
     * This allows the static methods in {@link AuthUtil} to access user data from the repository.
     *
     * <p>Note: Since {@link AuthUtil} relies on static methods, the repository is set using
     * this method during application startup. This approach ensures the utility class
     * has access to the repository without relying on Spring's dependency injection directly.
     *
     * @param userRepository the {@link UserRepository} instance managed by Spring
     */
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        AuthUtil.setUserRepository(userRepository);
    }
}
