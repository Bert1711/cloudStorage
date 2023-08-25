package ru.zaroyan.draftcloudstorage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Zaroyan
 */

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class OtherException extends RuntimeException {

    public OtherException(String message) {
        super(message);
    }
}
