package ru.zaroyan.draftcloudstorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zaroyan.draftcloudstorage.models.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author Zaroyan
 */
@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String username);
}
