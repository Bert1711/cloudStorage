package ru.zaroyan.draftcloudstorage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.repositories.UsersRepository;

/**
 * @author Zaroyan
 */
@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    public UserEntity login(String username, String password) {
        // Ваша реализация метода для авторизации пользователя
    }

    public void logout(String authToken) {
        // Ваша реализация метода для выхода из системы
    }
}

