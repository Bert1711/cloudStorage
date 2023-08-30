package ru.zaroyan.draftcloudstorage.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.services.FileService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Zaroyan
 */
@ExtendWith(MockitoExtension.class)
public class FileControllerTest {


    @InjectMocks
    private FileController fileController;

    @Mock
    private FileService fileService;



    @Test
    public void testUpload() throws Exception {
        String authToken = "your-auth-token";

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false); // Указываем, что ошибок нет


        // when
        doNothing().when(fileService).upload(Mockito.anyString(), Mockito.anyString(), Mockito.any(MultipartFile.class));
        ResponseEntity<HttpStatus> response = fileController.upload(authToken, "test.txt", file, bindingResult);

        // then
        verify(fileService, times(1)).upload(authToken, "test.txt", file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testEditFileName() {
        // Mock input data
        String authToken = "authToken";
        String oldFileName = "old.txt";
        String newFileName = "new.txt";

        // Mock service method
        doNothing().when(fileService).renameFile(eq(authToken), eq(oldFileName), eq(newFileName));

        // Perform the test
        ResponseEntity<String> response = fileController.editFileName(authToken, oldFileName, newFileName);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success upload", response.getBody());
    }

    @Test
    public void testDownload() {
        // Mock input data
        String authToken = "authToken";
        String filename = "example.txt";
        FileEntity mockFile = new FileEntity(); // Create a mock FileEntity as needed

        // Mock service method
        when(fileService.download(eq(filename), eq(authToken))).thenReturn(mockFile);

        // Perform the test
        ResponseEntity<?> response = fileController.download(filename, authToken);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testDeleteFile() {
        // Mock input data
        String authToken = "authToken";
        String filename = "example.txt";

        // Mock service method
        doNothing().when(fileService).deleteFile(eq(filename), eq(authToken));

        // Perform the test
        ResponseEntity<?> response = fileController.deleteFile(filename, authToken);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success deleted", response.getBody());
    }



    @Test
    public void testGetAllFiles() {
        // Mock input data
        String authToken = "authToken";
        List<FileEntity> mockFiles = new ArrayList<>(); // Create mock files as needed

        // Mock service method
        when(fileService.getAllFileForUser(eq(authToken))).thenReturn(mockFiles);

        // Perform the test
        ResponseEntity<?> response = fileController.getAllFiles(authToken);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}

