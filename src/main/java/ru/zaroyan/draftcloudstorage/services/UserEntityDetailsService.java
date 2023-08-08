package ru.zaroyan.draftcloudstorage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.repositories.UsersRepository;
import ru.zaroyan.draftcloudstorage.security.UserEntityDetails;

import java.util.Optional;

/**
 * @author Zaroyan
 */
@Service
public class UserEntityDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Autowired
    public UserEntityDetailsService(UsersRepository usersRepository)  {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = usersRepository.findByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("Пользователь не найден!");
        return new UserEntityDetails(user.get());
    }
}
