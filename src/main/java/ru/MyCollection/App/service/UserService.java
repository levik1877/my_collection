package ru.MyCollection.App.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.MyCollection.App.dto.User;
import ru.MyCollection.App.model.UserEntity;
import ru.MyCollection.App.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAllUsers() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public User getUserById(
            Long id
    ) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by id = " + id
                ));
        return toDto(userEntity);
    }

    @Transactional
    public User createUser(
            User userToCreate
    ) {
        if (userToCreate.id() != null) {
            throw new IllegalArgumentException("id should be empty");
        }
        if (repository.findByUsername(userToCreate.username()).isEmpty()) {
            throw new IllegalArgumentException("username = " + userToCreate.username() + " is already used");
        }
        if (repository.findByEmail(userToCreate.email()).isEmpty()) {
            throw new IllegalArgumentException("email = " + userToCreate.email() + " is already used");
        }

        var entityToSave = new UserEntity(
                null,
                userToCreate.role_id(),
                userToCreate.username(),
                userToCreate.passwordHash(),
                userToCreate.email(),
                userToCreate.emailVerifiedAt(),
                userToCreate.createdAt(),
                userToCreate.updateAt(),
                userToCreate.lastLoginAt(),
                null
        );

        return toDto(repository.save(entityToSave));
    }

    @Transactional
    public User updateUserById(
            Long id,
            User userToUpdate
    ) {
        var userEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by id = " + id
                ));

        if(userToUpdate.username().equals(userEntity.getUsername()) &&
                repository.findByUsername(userToUpdate.username()).isEmpty()
        ) {
            throw new IllegalArgumentException("username = " + userToUpdate.username() + " is already used");
        }
        if (userToUpdate.email().equals(userEntity.getEmail()) &&
                repository.findByEmail(userToUpdate.email()).isEmpty()) {
            throw new IllegalArgumentException("email = " + userToUpdate.email() + " is already used");
        }

        var userToSave = new UserEntity(
                userEntity.getId(),
                userToUpdate.role_id(),
                userToUpdate.username(),
                userToUpdate.passwordHash(),
                userToUpdate.email(),
                userToUpdate.emailVerifiedAt(),
                userToUpdate.createdAt(),
                userToUpdate.updateAt(),
                userToUpdate.lastLoginAt(),
                userToUpdate.isDeleted()
        );

        return toDto(repository.save(userToSave));
    }

    @Transactional
    private User deleteUserById(Long id) {
        var userEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by id = " + id
                ));

        userEntity.setIsDeleted(true);

        return toDto(repository.save(userEntity));
    }

    private User toDto(
            UserEntity userEntity
    ) {
        return new User(
                userEntity.getId(),
                userEntity.getRole_id(),
                userEntity.getUsername(),
                userEntity.getPasswordHash(),
                userEntity.getEmail(),
                userEntity.getEmailVerifiedAt(),
                userEntity.getCreatedAt(),
                userEntity.getUpdateAt(),
                userEntity.getLastLoginAt(),
                userEntity.getIsDeleted()
        );
    }
}
