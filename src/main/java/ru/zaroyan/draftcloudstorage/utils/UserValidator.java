package ru.zaroyan.draftcloudstorage.utils;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zaroyan.draftcloudstorage.models.UserEntity;

/**
 * @author Zaroyan
 */
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
