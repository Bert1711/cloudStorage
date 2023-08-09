package ru.zaroyan.draftcloudstorage.dto;

import lombok.Data;

/**
 * @author Zaroyan
 */
@Data
public class JwtRequest {
    private String username;
    private String password;
}