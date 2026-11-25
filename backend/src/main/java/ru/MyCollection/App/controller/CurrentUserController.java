package ru.MyCollection.App.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.MyCollection.App.dto.ProfileDto;
import ru.MyCollection.App.dto.UserDto;
import ru.MyCollection.App.repository.ProfileRepository;
import ru.MyCollection.App.repository.UserRepository;
import ru.MyCollection.App.service.ProfileService;
import ru.MyCollection.App.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CurrentUserController {
    private final UserRepository users;
    private final ProfileRepository profiles;
    private final UserService userService;
    private final ProfileService profileService;

    public record CurrentUserResponse(UserDto user, ProfileDto profile) {}

    @GetMapping("/user/me")
    @Transactional(readOnly = true)
    public CurrentUserResponse currentUser(Principal principal) {
        var user = users.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var profile = profiles.findFirstByOwnerIdAndIsDeletedFalseOrderByIdDesc(user.getId())
                .map(value -> profileService.getProfileById(value.getId())).orElse(null);
        return new CurrentUserResponse(userService.toDto(user), profile);
    }
}
