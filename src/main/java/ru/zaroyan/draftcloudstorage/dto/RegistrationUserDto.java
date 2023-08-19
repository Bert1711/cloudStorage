package ru.zaroyan.draftcloudstorage.dto;

import lombok.Data;
import ru.zaroyan.draftcloudstorage.models.FileEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zaroyan
 */
@Data
public class RegistrationUserDto {
    private String username;
    private String password;
    private String confirmPassword;

}
