package com.project.schoolmagazine.repository;

import com.project.schoolmagazine.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    Optional<UsersEntity> findByUsername(String username);
}
