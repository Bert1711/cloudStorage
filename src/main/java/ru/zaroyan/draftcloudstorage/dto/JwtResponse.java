package ru.zaroyan.draftcloudstorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
}
