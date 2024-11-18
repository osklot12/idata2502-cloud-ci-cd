package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.dto.RegisterRequest;
import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.exceptions.UserRegistrationException;
import ntnu.idata2502.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StandardUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StandardUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(RegisterRequest regRequest) {
        if (userRepository.existsByUsername(regRequest.username())) {
            throw new UserRegistrationException("User with username " + regRequest.username() + " already exists");
        }

        if (userRepository.existsByEmail(regRequest.email())) {
            throw new UserRegistrationException("User with email " + regRequest.email() + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(regRequest.password());
        User newUser = new User(regRequest.username(), regRequest.email(), encodedPassword);

        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
