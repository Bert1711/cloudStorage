package ru.zaroyan.draftcloudstorage.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.zaroyan.draftcloudstorage.dto.FileDto;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.repositories.FilesRepository;
import ru.zaroyan.draftcloudstorage.repositories.UsersRepository;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Zaroyan
 */


@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FilesRepository filesRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    private UserEntity user;
    private FileEntity file;

    @BeforeEach
    public void init() {
        user = UserEntity.builder().id(1L).login("user").password("password").build();
        file = FileEntity.builder()
                .id(10L)
                .name("test.txt")
                .fileType(MediaType.TEXT_PLAIN_VALUE)
                .size(13L)
                .bytes("dataFile".getBytes())
                .user(user)
                .build();
    }


    @Test
    public void testUpload() {
        String authToken = "auth-token";

        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(anyString())).thenReturn("user");
        when(usersRepository.findByLogin(anyString())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> fileService.upload(authToken, "test.txt", file));
        verify(filesRepository, times(1)).save(any(FileEntity.class));
    }

    @Test
    public void testDownload() {
        String authToken = "auth-token";
        String filename = "test.txt";

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(anyString())).thenReturn("user");
        when(usersRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(filesRepository.findFileByNameAndUser(filename, user)).thenReturn(Optional.of(file));

        assertEquals(file, fileService.download(filename, authToken));
    }

    @Test
    public void testDeleteFile() {
        String authToken = "auth-token";
        String filename = "test.txt";

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(anyString())).thenReturn("user");
        when(usersRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(filesRepository.findFileByNameAndUser(filename, user)).thenReturn(Optional.of(file));

        assertDoesNotThrow(() -> fileService.deleteFile(filename, authToken));
        verify(filesRepository, times(1)).delete(file);
    }

    @Test
    public void testRenameFile() {
        String authToken = "auth-token";
        String currentFileName = "test.txt";
        String newFileName = "new_test.txt";

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(anyString())).thenReturn("user");
        when(usersRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(filesRepository.findFileByNameAndUser(currentFileName, user)).thenReturn(Optional.of(file));

        assertDoesNotThrow(() -> fileService.renameFile(authToken, currentFileName, newFileName));
        assertEquals(newFileName, file.getName());
    }

    @Test
    public void testGetAllFileForUser() {
        String authToken = "auth-token";
        List<FileEntity> files = new ArrayList<>(); // Создаем список файлов

        FileEntity file1 = FileEntity.builder()
                .id(11L)
                .name("test1.txt")
                .fileType(MediaType.TEXT_PLAIN_VALUE)
                .size(13L)
                .bytes("dataFile".getBytes())
                .user(user)
                .build();
        FileEntity file2 = FileEntity.builder()
                .id(12L)
                .name("test2.txt")
                .fileType(MediaType.TEXT_PLAIN_VALUE)
                .size(13L)
                .bytes("dataFile".getBytes())
                .user(user)
                .build();
        FileEntity file3 = FileEntity.builder()
                .id(13L)
                .name("test3.txt")
                .fileType(MediaType.TEXT_PLAIN_VALUE)
                .size(13L)
                .bytes("dataFile".getBytes())
                .user(user)
                .build();

        files.add(file1);
        files.add(file2);
        files.add(file3);

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(anyString())).thenReturn("user");
        when(filesRepository.findFilesByUserIdWithLimit(user.getLogin(), 3)).thenReturn(files);
        List<FileDto> fileDtoList = files.stream()
                .map(file -> FileDto.builder()
                        .fileName(file.getName())
                        .created(file.getCreated())
                        .size(file.getSize())
                        .build()).collect(Collectors.toList());

        assertEquals(fileDtoList, fileService.getAllFiles(3, authToken));
    }

}

