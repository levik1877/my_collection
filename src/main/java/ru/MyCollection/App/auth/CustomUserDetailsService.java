package ru.MyCollection.App.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.MyCollection.App.dto.UserDto;
import ru.MyCollection.App.model.UserEntity;
import ru.MyCollection.App.repository.UserRepository;
import ru.MyCollection.App.service.RoleService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Not found user by username = " + username
                ));


        return new User(
                userEntity.getUsername(),
                userEntity.getPasswordHash(),
                userEntity.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(
                                role.getName()
                        )).toList()
        );
    }

    public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
        UserEntity userToSave = new UserEntity(
                null,
                null,
                registrationUserDto.getUsername(),
                passwordEncoder.encode(registrationUserDto.getPassword()),
                registrationUserDto.getEmail(),
                null,
                LocalDate.now(),
                null,
                LocalDate.now(),
                false
        );
        userToSave.setRoles(List.of(roleService.getRoleByName("ROLE_USER")));
        return toUserDto(userRepository.save(userToSave));
    }

    private UserDto toUserDto(
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
