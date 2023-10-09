package ru.zaroyan.draftcloudstorage.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.zaroyan.draftcloudstorage.dto.FileDto;
import ru.zaroyan.draftcloudstorage.exceptions.FileNotFoundExceptionImpl;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.repositories.FilesRepository;
import ru.zaroyan.draftcloudstorage.repositories.UsersRepository;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Zaroyan
 */

@Service
@Slf4j
@Transactional(readOnly = true)
public class FileService {
    private final FilesRepository filesRepository;
    private final UsersRepository usersRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final String tokenPrefix = "Bearer ";

    @Autowired
    public FileService(FilesRepository filesRepository,  UsersRepository usersRepository,
                       JwtTokenUtils jwtTokenUtils) {
        log.info("ИНИЦИАЛИЗАЦИЯ FileSer");
        this.filesRepository = filesRepository;
        this.usersRepository = usersRepository;
        this.jwtTokenUtils = jwtTokenUtils;

    }

    @Transactional
    public void upload(String authToken, String filename, MultipartFile resource) throws IOException {
        UserEntity user = getUserByToken(authToken);
        log.info("UserEntity user - выполнено" + user.toString());
        if(resource.isEmpty()) {
            log.info("Файл не найден");
            throw new RuntimeException("Error input data");
        }
        log.info(filename);
        FileEntity file = FileEntity.builder()
                .created(LocalDateTime.now())
                .name(filename)
                .fileType(resource.getContentType())
                .size(resource.getSize())
                .bytes(resource.getBytes())
                .user(user)
                .build();
        log.info("Файл build");
        log.info(file.toString());
        Optional<FileEntity> checkFilename = filesRepository.findByName(file.getName());
        log.info("checkRepository");
        if(checkFilename.isPresent()) {
            log.error("Файл с таким именем уже существует");
            throw new RuntimeException("The file with a such name already exist");

        }
        log.info("Файл уникален");
        filesRepository.save(file);
        log.info("Пользователь успешно загрузил файл {}", filename);
    }

    public FileEntity download(String filename, String authToken) {
        UserEntity user = getUserByToken(authToken);
        FileEntity file = filesRepository.findFileByNameAndUser(filename, user)
                .orElseThrow(() -> new FileNotFoundExceptionImpl("The file not found"));
        return file;
    }

    @Transactional
    public void deleteFile(String filename, String authToken) {
        UserEntity user = getUserByToken(authToken);

        Optional<FileEntity> fileOptional = filesRepository.findFileByNameAndUser(filename, user);
        if (fileOptional.isPresent()) {
            filesRepository.delete(fileOptional.get());
            log.info("Файл {} успешно удален", filename);
        } else {
            throw new FileNotFoundExceptionImpl("File not found");
        }

    }


    @Transactional
    public void renameFile(String authToken, String currentFileName,
                           String newFileName) {
        UserEntity user = getUserByToken(authToken);
        Optional<FileEntity> cloudFileOptional = filesRepository.findFileByNameAndUser(currentFileName, user);
        if (cloudFileOptional.isPresent()) {
            FileEntity file = cloudFileOptional.get();
            file.setName(newFileName);
            filesRepository.save(file);
            log.info("Имя файла было успешно изменено");
        } else {
            throw new FileNotFoundExceptionImpl("File not found");
        }
    }

    private FileEntity getFileByName(String filename, String authToken) {
        UserEntity user = getUserByToken(authToken);
        FileEntity cloudFile = filesRepository.findFileByNameAndUser(filename, user)
                .orElseThrow(() -> new RuntimeException("File can't be found for user:  "
                        + user.getLogin()
                ));
        return cloudFile;
    }


    private UserEntity getUserByToken(String authToken) {
        String token = authToken.substring(7);
        log.info(token);
        String username = jwtTokenUtils.validateTokenAndRetrieveClaim(token);
        log.info(username);
        return usersRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username
                        + " не найден"));
    }

    public List<FileDto> getAllFiles(int limit, String authToken) {
        String token = authToken.substring(7);

        log.info("вошли в метод getAllFiles");
        String login = jwtTokenUtils.validateTokenAndRetrieveClaim(token);
        log.info("после валидации токена");

        log.info("Поиск всех файлов в базе данных по Id пользователя: {} и лимиту вывода: {}", login, limit);
        List<FileEntity> listFiles = filesRepository.findFilesByUserIdWithLimit(login, limit);

        log.info("Все файлы в базе данных по Id пользователя: {} и лимиту вывода: {} найдены | Список файлов: {}", login, limit, listFiles);
        return listFiles.stream()
                .map(file -> FileDto.builder()
                        .fileName(file.getName())
                        .created(file.getCreated())
                        .size(file.getSize())
                        .build()).collect(Collectors.toList());
    }

//    public List<FileEntity> getAllFileForUser(int limit, String authToken){
//        UserEntity user = getUserByToken(authToken);
//        long userId = user.getId();
//        List<FileEntity> files = filesRepository.findFilesByUserIdWithLimit(userId, limit);
//        return files;
//    }

}

