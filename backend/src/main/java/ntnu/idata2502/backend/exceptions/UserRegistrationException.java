package ntnu.idata2502.backend.exceptions;

/**
 * An exception thrown when a user fails to register.
 */
public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException() {
        super();
    }

    public UserRegistrationException(String message) {
        super(message);
    }
}
