package com.project.schoolmagazine.repositories;

import com.project.schoolmagazine.entities.SubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectsRepository extends JpaRepository<SubjectsEntity, Integer> {
    @Query("SELECT s.subject FROM SubjectsEntity s")
    List<String> findAllSubjects();
}

