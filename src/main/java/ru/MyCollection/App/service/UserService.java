package ru.MyCollection.App.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.MyCollection.App.dto.UserDto;
import ru.MyCollection.App.dto.UserWithPasswordDto;
import ru.MyCollection.App.model.UserEntity;
import ru.MyCollection.App.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        return toDto(getUser(id));
    }

    // getPasswordById
    // setPasswordById нужно придумать как шифровать пароли перед отправкой на клиента

    @Transactional
    public UserDto createUser(UserWithPasswordDto userToCreate) {
        if (repository.findByUsername(userToCreate.getUsername()).isEmpty()) {
            throw new IllegalArgumentException("username = " + userToCreate.getUsername()+ " is already used");
        }
        if (repository.findByEmail(userToCreate.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("email = " + userToCreate.getEmail() + " is already used");
        }

        var entityToSave = new UserEntity(
                null,
                userToCreate.getRoles(), // вот эту хуйню надо проверять
                userToCreate.getUsername(),
                passwordEncoder.encode(userToCreate.getPassword()),
                userToCreate.getEmail(),
                null,
                LocalDate.now(),
                null,
                LocalDate.now(),
                false
        );

        return toDto(repository.save(entityToSave));
    }

    @Transactional
    public UserDto updateUserById(
            Long id,
            UserDto userToUpdate
    ) {
        var userEntity = getUser(id);

        if(userToUpdate.getUsername().equals(userEntity.getUsername()) &&
                repository.findByUsername(userToUpdate.getUsername()).isEmpty()
        ) {
            throw new IllegalArgumentException("username = " + userToUpdate.getUsername() + " is already used");
        }
        if (userToUpdate.getEmail().equals(userEntity.getEmail()) &&
                repository.findByEmail(userToUpdate.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("email = " + userToUpdate.getEmail() + " is already used");
        }

        var userToSave = new UserEntity(
                userEntity.getId(),
                userToUpdate.getRoles(), // И эту
                userToUpdate.getUsername(),
                userEntity.getPasswordHash(),
                userToUpdate.getEmail(),
                userToUpdate.getEmailVerifiedAt(),
                userToUpdate.getCreatedAt(),
                userToUpdate.getUpdateAt(),
                userToUpdate.getLastLoginAt(),
                userToUpdate.getIsDeleted()
        );

        return toDto(repository.save(userToSave));
    }

    @Transactional
    public UserDto deleteUserById(Long id) {
        var userEntity = getUser(id);
        userEntity.setIsDeleted(true);
        return toDto(repository.save(userEntity));
    }

    @Transactional
    public UserDto fullDeleteUserById(Long id) {
        var userEntity = getUser(id);
        repository.delete(getUser(id));
        return toDto(userEntity);
    }

    private UserEntity getUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found user by id = " + id
                ));
    }

    public UserDto toDto(
            UserEntity userEntity
    ) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getRoles(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getEmailVerifiedAt(),
                userEntity.getCreatedAt(),
                userEntity.getUpdateAt(),
                userEntity.getLastLoginAt(),
                userEntity.getIsDeleted()
        );
    }
}
