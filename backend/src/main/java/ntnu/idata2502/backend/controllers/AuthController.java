package ntnu.idata2502.backend.controllers;

import ntnu.idata2502.backend.dto.LoginRequest;
import ntnu.idata2502.backend.dto.RegisterRequest;
import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.exceptions.UserRegistrationException;
import ntnu.idata2502.backend.services.UserService;
import ntnu.idata2502.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor.
     *
     * @param userService the user service
     * @param jwtUtil the jwt utility class
     * @param authenticationManager the authentication manager
     */
    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Endpoint for user registration.
     *
     * @param request contains username and password
     * @return success message if registration is successful
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            User newUser = userService.registerUser(request);
            response.put("message", "User registered successfully.");
            return ResponseEntity.ok(response);
        } catch (UserRegistrationException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Endpoint for user login.
     *
     * @param request contains username and password
     * @return JWT token if login is successful
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            String token = jwtUtil.generateToken(request.username());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            response.put("error", "Incorrect username or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
