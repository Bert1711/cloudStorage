package ru.zaroyan.draftcloudstorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zaroyan.draftcloudstorage.models.UserEntity;

import java.util.List;

/**
 * @author Zaroyan
 */
@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByUsername(String username);
}
