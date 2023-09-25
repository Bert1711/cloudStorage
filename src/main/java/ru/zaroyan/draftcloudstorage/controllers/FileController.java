package ru.zaroyan.draftcloudstorage.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.zaroyan.draftcloudstorage.dto.FileDto;
import ru.zaroyan.draftcloudstorage.exceptions.ValidationException;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.services.FileService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zaroyan
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@AllArgsConstructor
public class FileController {

    private FileService fileService;

    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> upload(@RequestHeader("auth-token") String authToken,
                                             @RequestParam("filename") String filename,
                                             @Valid MultipartFile multipartFile,
                                             BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Validation failed");
        }

        fileService.upload(authToken, filename, multipartFile);

        return ResponseEntity.ok(HttpStatus.OK);


    }

    @PutMapping("/file")
    public ResponseEntity<String> editFileName(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String oldFileName,
                                               @RequestBody String newFileName) {
        fileService.renameFile(authToken, oldFileName, newFileName);
        return new ResponseEntity<>("Success upload", HttpStatus.OK);
    }


    @GetMapping("/file")
    public ResponseEntity<?> download(@RequestParam("filename") String filename,
                                      @RequestHeader("auth-token") String authToken) {

        FileEntity file = fileService.download(filename, authToken);
        if (file != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + file.getName())
                    .body(file);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public List<FileDto> getFileList(Principal principal,
                                     @RequestParam(name = "limit", defaultValue = "5") int limit) {
        log.info("GET Request: get FileList " + limit);
        return new ArrayList<>();
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename,
                                             @RequestHeader("auth-token") String authToken) {

        fileService.deleteFile(filename, authToken);

        return ResponseEntity.ok("Success deleted");

    }
}
