package ru.zaroyan.draftcloudstorage.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.services.UserService;

/**
 * @author Zaroyan
 */
@Component
public class UserValidator implements Validator {

    private final UserService usersService;

    @Autowired
    public UserValidator(UserService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserEntity user = (UserEntity) target;
        // Проверка уникальности имени пользователя (логина)
        if (usersService.getUserByName(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "Человек с таким именем пользователя уже существует");
        }
    }
}
