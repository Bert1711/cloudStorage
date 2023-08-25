package ru.zaroyan.draftcloudstorage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.repositories.UsersRepository;

/**
 * @author Zaroyan
 */
@Service
public class RegistrationService {

    private final UsersRepository usersRepository;

    @Autowired
    public RegistrationService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional(readOnly = true)
    public void register(UserEntity user) {
        usersRepository.save(user);
    }
}
