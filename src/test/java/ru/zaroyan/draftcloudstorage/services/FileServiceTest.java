package ru.zaroyan.draftcloudstorage.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.repositories.FilesRepository;
import ru.zaroyan.draftcloudstorage.repositories.UsersRepository;
import ru.zaroyan.draftcloudstorage.utils.JwtTokenUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    @Test
    public void testUpload() throws IOException {
        String authToken = "your-auth-token";
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(authToken)).thenReturn("username");
        when(usersRepository.findByLogin("username")).thenReturn(Optional.of(new UserEntity()));
        when(filesRepository.findFileByName("test.txt")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> fileService.upload(authToken, "test.txt", file));
        verify(filesRepository, times(1)).save(any(FileEntity.class));
    }

    @Test
    public void testDownload() {
        String authToken = "your-auth-token";
        String filename = "test.txt";
        UserEntity user = new UserEntity();
        FileEntity file = new FileEntity();

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(authToken)).thenReturn("username");
        when(usersRepository.findByLogin("username")).thenReturn(Optional.of(user));
        when(filesRepository.findFileByNameAndUser(filename, user)).thenReturn(Optional.of(file));

        assertEquals(file, fileService.download(filename, authToken));
    }

    @Test
    public void testDeleteFile() {
        String authToken = "your-auth-token";
        String filename = "test.txt";
        UserEntity user = new UserEntity();
        FileEntity file = new FileEntity();

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(authToken)).thenReturn("username");
        when(usersRepository.findByLogin("username")).thenReturn(Optional.of(user));
        when(filesRepository.findFileByNameAndUser(filename, user)).thenReturn(Optional.of(file));

        assertDoesNotThrow(() -> fileService.deleteFile(filename, authToken));
        verify(filesRepository, times(1)).delete(file);
    }

    @Test
    public void testRenameFile() {
        String authToken = "your-auth-token";
        String currentFileName = "test.txt";
        String newFileName = "new_test.txt";
        UserEntity user = new UserEntity();
        FileEntity file = new FileEntity();

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(authToken)).thenReturn("username");
        when(usersRepository.findByLogin("username")).thenReturn(Optional.of(user));
        when(filesRepository.findFileByNameAndUser(currentFileName, user)).thenReturn(Optional.of(file));

        assertDoesNotThrow(() -> fileService.renameFile(authToken, currentFileName, newFileName));
        assertEquals(newFileName, file.getName());
    }

    @Test
    public void testGetAllFileForUser() {
        String authToken = "your-auth-token";
        UserEntity user = new UserEntity();
        List<FileEntity> files = new ArrayList<>();

        when(jwtTokenUtils.validateTokenAndRetrieveClaim(authToken)).thenReturn("username");
        when(usersRepository.findByLogin("username")).thenReturn(Optional.of(user));
        when(filesRepository.findAllByUserOrderByCreatedDesc(user)).thenReturn(files);

        assertEquals(files, fileService.getAllFileForUser(authToken));
    }
}

