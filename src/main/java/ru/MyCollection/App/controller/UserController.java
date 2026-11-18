package ru.MyCollection.App.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.MyCollection.App.dto.User;
import ru.MyCollection.App.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Called method getAllUsers");
        return ResponseEntity.status(200).body(userService.findAllUsers());
    }

    @PostMapping()
    public ResponseEntity<User> createUser(
        @RequestBody User userToCreate
    ) {
        log.info("Called method createUser");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userToCreate));
    }
}
