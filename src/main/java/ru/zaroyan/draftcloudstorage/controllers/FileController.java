package ru.zaroyan.draftcloudstorage.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.zaroyan.draftcloudstorage.dto.FileDto;
import ru.zaroyan.draftcloudstorage.models.FileEntity;
import ru.zaroyan.draftcloudstorage.services.FileService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Zaroyan
 */
@RestController
@Slf4j
public class FileController {

    private final FileService fileService;
    private final ModelMapper modelMapper;

    @Autowired
    public FileController(FileService fileService, ModelMapper modelMapper) {
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/file")
    public ResponseEntity<HttpStatus> upload(@RequestParam("filename") String filename,
                                             @RequestPart("file") MultipartFile file,
                                             @RequestHeader("auth-token") String authToken) throws IOException{

        log.info(filename);
        if (file == null)
            log.info("ПУСТО!");

        fileService.upload(authToken, filename, file);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/file")
    public ResponseEntity<String> editFileName(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String oldFileName,
                                               @RequestBody Map<String, String> newFileNameJSON) {
        String newFileName = newFileNameJSON.get("filename");
        fileService.renameFile(authToken, oldFileName, newFileName);
        return new ResponseEntity<>("Success upload", HttpStatus.OK);
    }


    @GetMapping("/file")
    public ResponseEntity<?> download(@RequestParam("filename") String filename,
                                      @RequestHeader("auth-token") String authToken) {

        FileEntity file = fileService.download(filename, authToken);
        FileDto fileDto = convertToFileDto(file);
        if (file != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + file.getName())
                    .body(fileDto);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllFiles(@RequestParam(name = "limit", defaultValue = "3") int limit,
                                         @RequestHeader("auth-token") String authToken) {
        log.info("limit"+limit);
        log.info(authToken);

        List<FileDto> files = fileService.getAllFiles(limit, authToken);

        return ResponseEntity.ok(files);

    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename,
                                             @RequestHeader("auth-token") String authToken) {
        fileService.deleteFile(filename, authToken);

        return ResponseEntity.ok("Success deleted");

    }

    public FileDto convertToFileDto(FileEntity fileEntity) {
        return this.modelMapper.map(fileEntity, FileDto.class);
    }
}
