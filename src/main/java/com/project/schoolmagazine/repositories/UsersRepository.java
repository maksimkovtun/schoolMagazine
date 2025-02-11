package com.project.schoolmagazine.repositories;

import com.project.schoolmagazine.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    Optional<UsersEntity> findByUsername(String username);
    List<UsersEntity> findByUsernameContainingIgnoreCase(String username);
    Optional<UsersEntity> findByUserId(Integer userId);
}


