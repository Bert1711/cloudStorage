package ru.zaroyan.draftcloudstorage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.repositories.UsersRepository;

import java.util.Optional;

/**
 * @author Zaroyan
 */
@Service
public class UserService {
    UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Optional<UserEntity> getUserByName(String username) {
        return usersRepository.findByUsername(username);
    }
}
