package ru.zaroyan.draftcloudstorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.models.UserEntity;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * @author Zaroyan
 */
@Repository
public interface FilesRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findFileByName(String name);
    Optional<FileEntity> findFileByNameAndUser(String filename, UserEntity user);
    @Query(value = "SELECT * FROM files f WHERE f.user_login = ?1 ORDER BY f.id DESC LIMIT ?2", nativeQuery = true)
    List<FileEntity> findFilesByUserIdWithLimit(String login, int limit);



    List<FileEntity> findAllByUserId(Long userId);

    List<FileEntity> findAllByUserIdAndNameLike(Long userId, String firstLetters); //поиск по совпадающим первым буквам имени файла

    List<FileEntity> findAllByUserIdOrderByName(Long userId); // Сортировка по имени в возрастающем порядке


    List<FileEntity> findAllByUserIdOrderBySizeDesc(Long userId); // Сортировка по размеру в убывающем порядке

    List<FileEntity> findByUser(UserEntity user);

    List<FileEntity> findAllByUserAndNameLike(UserEntity user, String firstLetters);

    List<FileEntity> findAllByUserOrderByName(UserEntity user);


    List<FileEntity> findAllByUserOrderBySizeDesc(UserEntity user);
}



