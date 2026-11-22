package ru.MyCollection.App.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.MyCollection.App.dto.UserDto;
import ru.MyCollection.App.model.UserEntity;
import ru.MyCollection.App.repository.UserRepository;

@Service
@Transactional
public class UserService {
    private final UserRepository repository;


    public UserService(UserRepository repository, RoleService roleService) {
        this.repository = repository;
    }


//    public List<User> findAllUsers() {
//        return repository.findAll().stream().map(this::toDto).toList();
//    }

//    public User getUserById(
//            Long id
//    ) {
//        UserEntity userEntity = repository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "Not found user by id = " + id
//                ));
//        return toDto(userEntity);
//    }
//
//    public User createUser(
//            UserToCreate userToCreate
//    ) {
//        if (repository.findByUsername(userToCreate.username()).isEmpty()) {
//            throw new IllegalArgumentException("username = " + userToCreate.username() + " is already used");
//        }
//        if (repository.findByEmail(userToCreate.email()).isEmpty()) {
//            throw new IllegalArgumentException("email = " + userToCreate.email() + " is already used");
//        }
//
//        var entityToSave = new UserEntity(
//                null,
//                userToCreate.username(),
//                userToCreate.passwordHash(),
//                userToCreate.email(),
//                null,
//                LocalDate.now(),
//                null,
//                LocalDate.now(),
//                null
//        );
//
//        return toDto(repository.save(entityToSave));
//    }

//    public User updateUserById(
//            Long id,
//            User userToUpdate
//    ) {
//        var userEntity = repository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "Not found user by id = " + id
//                ));
//
//        if(userToUpdate.username().equals(userEntity.getUsername()) &&
//                repository.findByUsername(userToUpdate.username()).isEmpty()
//        ) {
//            throw new IllegalArgumentException("username = " + userToUpdate.username() + " is already used");
//        }
//        if (userToUpdate.email().equals(userEntity.getEmail()) &&
//                repository.findByEmail(userToUpdate.email()).isEmpty()) {
//            throw new IllegalArgumentException("email = " + userToUpdate.email() + " is already used");
//        }
//
//        var userToSave = new UserEntity(
//                userEntity.getId(),
//                userToUpdate.username(),
//                userToUpdate.passwordHash(),
//                userToUpdate.email(),
//                userToUpdate.emailVerifiedAt(),
//                userToUpdate.createdAt(),
//                userToUpdate.updateAt(),
//                userToUpdate.lastLoginAt(),
//                userToUpdate.isDeleted()
//        );
//
//        return toDto(repository.save(userToSave));
//    }
//
//    @Transactional
//    private User deleteUserById(Long id) {
//        var userEntity = repository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "Not found user by id = " + id
//                ));
//
//        userEntity.setIsDeleted(true);
//
//        return toDto(repository.save(userEntity));
//    }
//
    public UserDto toDto(
            UserEntity userEntity
    ) {
        return new UserDto(
                userEntity.getId(),
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
