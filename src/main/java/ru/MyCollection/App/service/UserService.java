package ru.MyCollection.App.service;

import org.springframework.stereotype.Service;
import ru.MyCollection.App.dto.User;
import ru.MyCollection.App.model.UserEntity;
import ru.MyCollection.App.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        List<UserEntity> allUsersEntities = userRepository.findAll();

        return allUsersEntities.stream()
                .map(it ->
                    new User(
                            it.getId(),
                            it.getRole_id(),
                            it.getUsername(),
                            it.getPasswordHash(),
                            it.getEmail(),
                            it.getEmailVerifiedAt(),
                            it.getCreatedAt(),
                            it.getUpdateAt(),
                            it.getLastLoginAt(),
                            it.getIsDeleted()
                    )
                ).toList();
    }
}
