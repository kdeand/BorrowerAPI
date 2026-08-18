package org.dean.borrower.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.dean.borrower.dto.UserRequest;
import org.dean.borrower.dto.UserResponse;
import org.dean.borrower.entity.User;
import org.dean.borrower.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Get all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //Creating user
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest user) {
        UserResponse createdUser = userService.createUser(user);

        return ResponseEntity.status(201).body(createdUser);
    }

    //Read one
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        UserResponse user = userService.getUserById(id);

        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    //update
    @PostMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest user) {
        UserResponse updatedUser = userService.updateUser(id, user);
        if(updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedUser);
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);

        if(!deleted) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
