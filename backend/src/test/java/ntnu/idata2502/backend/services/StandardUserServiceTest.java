package ntnu.idata2502.backend.services;

import ntnu.idata2502.backend.dto.RegisterRequest;
import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
@SpringBootTest
class StandardUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StandardUserService userService;

    @Test
    void testRegisterUserSuccess() {
        // given
        RegisterRequest request = new RegisterRequest("username", "email@test.com", "password");
        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        User savedUser = new User("username", "email@test.com", "encodedPassword");
        when(userRepository.save(Mockito.argThat(Objects::nonNull))).thenReturn(savedUser);

        // when
        User result = userService.registerUser(request);

        // then
        assertNotNull(result);
        assertEquals("username", result.getUsername());
        assertEquals("email@test.com", result.getEmail());
        verify(userRepository).save(Mockito.argThat(Objects::nonNull));
    }
}