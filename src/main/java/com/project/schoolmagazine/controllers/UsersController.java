package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.entities.UsersEntity;
import com.project.schoolmagazine.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<UsersEntity> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<UsersEntity> getUserById(@PathVariable Integer id) {
        return usersService.getUserById(id);
    }

    @PostMapping
    public UsersEntity addUser(@RequestBody UsersEntity user) {
        return usersService.saveUser(user);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        try {
            usersService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public List<UsersEntity> searchUsersByEmail(@RequestParam String email) {
        return usersService.searchUsersByEmail(email);
    }

    @PutMapping("/{id}")
    public UsersEntity updateUser(@PathVariable Integer id, @RequestBody UsersEntity updatedUser) {
        return usersService.updateUser(id, updatedUser);
    }
}
