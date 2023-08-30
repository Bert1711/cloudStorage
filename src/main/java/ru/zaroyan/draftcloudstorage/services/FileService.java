package ru.zaroyan.draftcloudstorage.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.zaroyan.draftcloudstorage.exceptions.FileNotFoundExceptionImpl;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.repositories.FilesRepository;
import ru.zaroyan.draftcloudstorage.repositories.UsersRepository;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        this.filesRepository = filesRepository;
        this.usersRepository = usersRepository;
        this.jwtTokenUtils = jwtTokenUtils;

    }

    @Transactional
    public void upload(String authToken, String filename, MultipartFile resource) throws IOException {
        UserEntity user = getUserByToken(authToken);
        if(resource.isEmpty()) {
            log.info("Файл не найден");
            throw new RuntimeException("Error input data");
        }
        FileEntity file = FileEntity.builder()
                .name(filename)
                .fileType(resource.getContentType())
                .size(resource.getSize())
                .bytes(resource.getBytes())
                .user(user)
                .build();
        Optional<FileEntity> checkFilename = filesRepository.findFileByName(file.getName());
        if(checkFilename.isPresent()) {
            log.error("Файл с таким именем уже существует");
            throw new RuntimeException("The file with a such name already exist");

        }
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
                        + user.getUsername()
                ));
        return cloudFile;
    }


    private UserEntity getUserByToken(String authToken) {
        String username = jwtTokenUtils.validateTokenAndRetrieveClaim(authToken.replace
                (tokenPrefix, ""));
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username
                        + " не найден"));



    }

    public List<FileEntity> getAllFileForUser(String authToken){
        UserEntity user = getUserByToken(authToken);
        List<FileEntity> files = filesRepository.findAllByUserOrderByCreatedDesc(user);
        return files;
    }

}

