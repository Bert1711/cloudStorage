package ru.zaroyan.draftcloudstorage.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.zaroyan.draftcloudstorage.models.UserEntity;
import ru.zaroyan.draftcloudstorage.repositories.UsersRepository;
import ru.zaroyan.draftcloudstorage.security.UserEntityDetails;

import java.util.Optional;

/**
 * @author Zaroyan
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Autowired
    public UserDetailsServiceImpl(UsersRepository usersRepository)  {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Запрошена загрузка пользователя с именем: {}", username);

        // Выполните поиск пользователя в репозитории
        Optional<UserEntity> user = usersRepository.findByLogin(username);

        if (user.isPresent()) {
            log.info("Пользователь с именем {} найден", username);
            return new UserEntityDetails(user.get());
        } else {
            log.warn("Пользователь с именем {} не найден", username);
            throw new UsernameNotFoundException("Пользователь не найден!");
        }
    }
}
