    package com.project.schoolmagazine.repositories;

    import com.project.schoolmagazine.entities.ProfilesEntity;
    import com.project.schoolmagazine.entities.UsersEntity;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.stereotype.Repository;

    import java.util.List;
    import java.util.Optional;

    @Repository
    public interface ProfilesRepository extends JpaRepository<ProfilesEntity, Integer> {
        Optional<ProfilesEntity> findProfileByUser(UsersEntity user);

        @Query("SELECT CONCAT(p.classNumber, p.classLetter) FROM ProfilesEntity p GROUP BY p.classNumber, p.classLetter ORDER BY p.classNumber, p.classLetter")
        List<String> findAllClasses();

        @Query("SELECT CONCAT(p.classNumber, p.classLetter) FROM ProfilesEntity p WHERE p.user.username = :username")
        Optional<String> findClassByUserName(String username);

    }

