package ru.zaroyan.draftcloudstorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
public class JWTResponse {
    @JsonProperty("auth-token")
    private String authToken;

}
