package ru.zaroyan.draftcloudstorage.exceptions;

/**
 * @author Zaroyan
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String msg) {
        super(msg);
    }
}
