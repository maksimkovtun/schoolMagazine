package com.project.schoolmagazine.repository;

import com.project.schoolmagazine.entity.StudentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsRepository extends JpaRepository<StudentsEntity, Integer> {
    // Можно добавить дополнительные методы, например, поиск по имени
    // List<StudentsEntity> findByStudentName(String studentName);
}
