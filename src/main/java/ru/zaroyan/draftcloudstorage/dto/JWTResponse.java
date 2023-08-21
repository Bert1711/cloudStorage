package ru.zaroyan.draftcloudstorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
public class JWTResponse {
    @JsonProperty("auth-token")
    private final String authToken;
}
