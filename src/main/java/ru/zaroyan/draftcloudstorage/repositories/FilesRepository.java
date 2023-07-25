package ru.zaroyan.draftcloudstorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.models.UserEntity;

import java.util.List;

/**
 * @author Zaroyan
 */
@Repository
public interface FilesRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findAllByUserId(Long userId);

    List<FileEntity> findAllByUserIdAndNameLike(Long userId, String firstLetters); //поиск по совпадающим первым буквам имени файла

    List<FileEntity> findAllByUserIdOrderByName(Long userId); // Сортировка по имени в возрастающем порядке

    List<FileEntity> findAllByUserIdOrderByType(Long userId); // Сортировка по типу в возрастающем порядке

    List<FileEntity> findAllByUserIdOrderBySizeDesc(Long userId); // Сортировка по размеру в убывающем порядке

    List<FileEntity> findByUser(UserEntity user);

    List<FileEntity> findAllByUserAndNameLike(UserEntity user, String firstLetters);

    List<FileEntity> findAllByUserOrderByName(UserEntity user);

    List<FileEntity> findAllByUserOrderByType(UserEntity user);

    List<FileEntity> findAllByUserOrderBySizeDesc(UserEntity user);
}



