package ru.MyCollection.App.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.MyCollection.App.dto.ProfileDto;
import ru.MyCollection.App.service.ProfileService;
import ru.MyCollection.App.repository.UserRepository;
import ru.MyCollection.App.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;

@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final ProfileService profileService;
    private final UserRepository users;
    private final ProfileRepository profiles;

    @PostMapping("/create_profile")
    public ResponseEntity<ProfileDto> createProfile(
            @Valid @RequestBody ProfileDto profileDto, Principal principal
    ) {
        var user = users.findByUsername(principal.getName()).orElseThrow();
        if (!user.getId().equals(profileDto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot create another user's profile");
        }
        if (profiles.findFirstByOwnerIdAndIsDeletedFalseOrderByIdDesc(user.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Profile already exists");
        }
        return ResponseEntity.ok(profileService.createProfile(profileDto));
    }
}
