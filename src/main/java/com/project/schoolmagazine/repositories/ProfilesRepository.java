    package com.project.schoolmagazine.repositories;

    import com.project.schoolmagazine.entities.ProfilesEntity;
    import com.project.schoolmagazine.entities.UsersEntity;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface ProfilesRepository extends JpaRepository<ProfilesEntity, Integer> {
        Optional<ProfilesEntity> findProfileByUser(UsersEntity user);
    }

