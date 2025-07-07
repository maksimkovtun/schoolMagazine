package com.project.schoolmagazine.repositories;

import com.project.schoolmagazine.entities.ScoresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoresRepository extends JpaRepository<ScoresEntity, Integer> { }

