package ru.zaroyan.draftcloudstorage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zaroyan
 */
@RestController
@RequiredArgsConstructor
public class FileController {


    @GetMapping("/list")
    public String getFilesList() {
        return "List of Files";
    }
    @GetMapping("/file")
    public String unsecuredData() {
        return "File";
    }
    @GetMapping("/secured")
    public String securedData() {
        return "Secured data";
    }
}
