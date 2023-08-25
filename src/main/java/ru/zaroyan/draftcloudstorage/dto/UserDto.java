package ru.zaroyan.draftcloudstorage.dto;

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
public class UserDto {
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    private String username;
    @Size(min = 6, max = 20, message = "Длина пароля должна быть от 6 до 20 символов")
    @Column(nullable = false)
    private String password;

}
