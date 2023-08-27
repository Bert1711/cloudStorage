package ru.zaroyan.draftcloudstorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTResponse {
    @JsonProperty("auth-token")
    private String authToken;

}
