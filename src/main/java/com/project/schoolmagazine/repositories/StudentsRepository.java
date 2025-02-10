    package com.project.schoolmagazine.repositories;

    import com.project.schoolmagazine.entities.StudentsEntity;
    import com.project.schoolmagazine.entities.UsersEntity;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface StudentsRepository extends JpaRepository<StudentsEntity, Integer> {
        Optional<StudentsEntity> findStudentByUser(UsersEntity user);
    }

