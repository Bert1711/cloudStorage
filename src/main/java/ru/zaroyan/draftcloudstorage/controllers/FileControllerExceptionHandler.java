package ru.zaroyan.draftcloudstorage.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zaroyan.draftcloudstorage.exceptions.FileNotFoundExceptionImpl;
import ru.zaroyan.draftcloudstorage.exceptions.OtherException;
import ru.zaroyan.draftcloudstorage.exceptions.UnauthorizedException;
import ru.zaroyan.draftcloudstorage.exceptions.ValidationException;

/**
 * @author Zaroyan
 */

@ControllerAdvice
public class FileControllerExceptionHandler {

    @ExceptionHandler(FileNotFoundExceptionImpl.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundExceptionImpl ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + ex.getMessage());
    }

    @ExceptionHandler(OtherException.class)
    public ResponseEntity<String> handleOtherException(OtherException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + ex.getMessage());
    }

}

