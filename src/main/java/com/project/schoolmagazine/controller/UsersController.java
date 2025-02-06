package com.project.schoolmagazine.controller;

import com.project.schoolmagazine.entity.UsersEntity;
import com.project.schoolmagazine.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        usersService.deleteUser(id);
    }
}
