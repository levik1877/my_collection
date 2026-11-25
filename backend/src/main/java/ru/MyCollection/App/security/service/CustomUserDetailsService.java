package ru.MyCollection.App.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.MyCollection.App.dto.RegistrationUserDto;
import ru.MyCollection.App.dto.UserDto;
import ru.MyCollection.App.model.UserEntity;
import ru.MyCollection.App.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.MyCollection.App.service.RoleService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "Not found user by username = " + username
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = findByUsername(username);
        return new User(
                userEntity.getUsername(),
                userEntity.getPasswordHash(),
                userEntity.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(
                                role.getName()
                        )).toList()
        );
    }

    @Transactional
    public UserDto setLastLoginDateUserByUsername(String username) {
        var userEntity = findByUsername(username);
        userEntity.setLastLoginAt(LocalDate.now());
        return toUserDto(userEntity);
    }

    @Transactional
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
        userToSave.setRoles(List.of(roleService.getRoleByName("ROLE_USER"))); // Когда будет подтверждение email не нужно выдавать роль user. Роль выдаётся после подтверждения email
        return toUserDto(userRepository.save(userToSave));
    }

    private UserDto toUserDto(
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
