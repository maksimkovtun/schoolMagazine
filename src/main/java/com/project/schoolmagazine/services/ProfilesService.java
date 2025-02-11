package com.project.schoolmagazine.services;

import com.project.schoolmagazine.entities.ProfilesEntity;
import com.project.schoolmagazine.repositories.ProfilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfilesService {

    private final ProfilesRepository profilesRepository;

    @Autowired
    public ProfilesService(ProfilesRepository profilesRepository) {
        this.profilesRepository = profilesRepository;
    }

    public List<ProfilesEntity> getAllProfiles() {
        return profilesRepository.findAll();
    }

    public Optional<ProfilesEntity> getProfileById(Integer id) {
        return profilesRepository.findById(id);
    }

    public ProfilesEntity saveProfile(ProfilesEntity profile) {
        return profilesRepository.save(profile);
    }

    public void deleteProfile(Integer id) {
        profilesRepository.deleteById(id);
    }
}
