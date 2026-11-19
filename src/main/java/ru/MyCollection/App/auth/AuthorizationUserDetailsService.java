package ru.MyCollection.App.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.MyCollection.App.model.UserEntity;
import ru.MyCollection.App.repository.UserRepository;
import ru.MyCollection.App.service.RoleService;
import ru.MyCollection.App.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);

    }

    @Override
    @Transactional
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

    public void createNewUser(UserEntity userEntity) {
        userEntity.setRoles(List.of(roleService.getRoleByName("user")));
        userRepository.save(userEntity);
    }
}
