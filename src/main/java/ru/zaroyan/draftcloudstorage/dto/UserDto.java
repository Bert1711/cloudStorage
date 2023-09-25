package ru.zaroyan.draftcloudstorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zaroyan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UserDto {

    private String login;

    private String password;

    public String getUsername() {
        return login;
    }

    public void setUsername(String username) {
        this.login = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
