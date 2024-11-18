package ntnu.idata2502.backend.controllers;

import ntnu.idata2502.backend.entities.User;
import ntnu.idata2502.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findUserByUsername(username);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("message", "User with username '" + username + "' not found.");
            errorBody.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
        }
    }
}
