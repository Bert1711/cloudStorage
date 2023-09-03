package ru.zaroyan.draftcloudstorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Zaroyan
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

}
