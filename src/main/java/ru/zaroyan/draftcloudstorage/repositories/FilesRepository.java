package ru.zaroyan.draftcloudstorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.models.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author Zaroyan
 */
@Repository
public interface FilesRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findFileByNameAndUser(String filename, UserEntity user);

    @Query(value = "SELECT * FROM files f WHERE f.user_login = ?1 ORDER BY f.id DESC LIMIT ?2", nativeQuery = true)
    List<FileEntity> findFilesByUserIdWithLimit(String login, int limit);
}




