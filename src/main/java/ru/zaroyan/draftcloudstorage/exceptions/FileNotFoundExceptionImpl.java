package ru.zaroyan.draftcloudstorage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Zaroyan
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundExceptionImpl extends RuntimeException {

    public FileNotFoundExceptionImpl(String message) {
        super(message);
    }
}

