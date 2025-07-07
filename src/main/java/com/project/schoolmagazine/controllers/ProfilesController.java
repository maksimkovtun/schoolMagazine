package com.project.schoolmagazine.controllers;

import com.project.schoolmagazine.entities.ProfilesEntity;
import com.project.schoolmagazine.services.ProfilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
public class ProfilesController {
    private final ProfilesService profilesService;
    @Autowired
    public ProfilesController(ProfilesService profilesService) {
        this.profilesService = profilesService;
    }
    @GetMapping
    public List<ProfilesEntity> getAllProfiles() {
        return profilesService.getAllProfiles();
    }
    @GetMapping("/{id}")
    public Optional<ProfilesEntity> getProfileById(@PathVariable Integer id) {
        return profilesService.getProfileById(id);
    }
    @PostMapping
    public ProfilesEntity addProfile(@RequestBody ProfilesEntity profile) {
        return profilesService.saveProfile(profile);
    }
    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Integer id) {
        profilesService.deleteProfile(id);
    }
}
