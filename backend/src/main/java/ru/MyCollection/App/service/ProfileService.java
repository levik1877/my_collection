package ru.MyCollection.App.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.MyCollection.App.dto.ProfileDto;
import ru.MyCollection.App.model.ProfileEntity;
import ru.MyCollection.App.repository.ProfileRepository;
import ru.MyCollection.App.repository.UserRepository;
import ru.MyCollection.App.repository.UploadedFileRepository;
import ru.MyCollection.App.repository.LocationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;
    private final UserRepository userRepository;
    private final UploadedFileRepository uploadedFileRepository;
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<ProfileDto> findAllProfiles() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ProfileDto getProfileById(Long id) {
        return toDto(getProfile(id));
    }

    @Transactional
    public ProfileDto createProfile(ProfileDto profileToCreate) {
        var entityToSave = new ProfileEntity(
                null,
                userRepository.findById(profileToCreate.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Not found user by id = " + profileToCreate.getUserId()
                        )),
                profileToCreate.getPublicName(),
                profileToCreate.getAvatarFileId() == null ? null :
                        uploadedFileRepository.findById(profileToCreate.getAvatarFileId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Not found file by id = " + profileToCreate.getAvatarFileId()
                                )),
                profileToCreate.getLocationId() == null ? null :
                        locationRepository.findById(profileToCreate.getLocationId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Not found location by id = " + profileToCreate.getLocationId()
                                )),
                0,
                profileToCreate.getDescription(),
                LocalDate.now(),
                false
        );

        return toDto(repository.save(entityToSave));
    }

    @Transactional
    public ProfileDto updateProfileById(
            Long id,
            ProfileDto profileToUpdate
    ) {
        var profileEntity = getProfile(id);

        var profileToSave = new ProfileEntity(
                profileEntity.getId(),
                userRepository.findById(profileToUpdate.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Not found user by id = " + profileToUpdate.getUserId()
                        )),
                profileToUpdate.getPublicName(),
                profileToUpdate.getAvatarFileId() == null ? null :
                        uploadedFileRepository.findById(profileToUpdate.getAvatarFileId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Not found file by id = " + profileToUpdate.getAvatarFileId()
                                )),
                profileToUpdate.getLocationId() == null ? null :
                        locationRepository.findById(profileToUpdate.getLocationId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Not found location by id = " + profileToUpdate.getLocationId()
                                )),
                profileToUpdate.getReputation(),
                profileToUpdate.getDescription(),
                profileToUpdate.getUpdateAt(),
                profileToUpdate.getIsDeleted()
        );

        return toDto(repository.save(profileToSave));
    }

    @Transactional
    public ProfileDto deleteProfileById(Long id) {
        var profileEntity = getProfile(id);
        profileEntity.setIsDeleted(true);
        return toDto(repository.save(profileEntity));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProfileDto finalDeleteProfileById(Long id) {
        var profileEntity = getProfile(id);
        repository.delete(profileEntity);
        return toDto(profileEntity);
    }

    private ProfileEntity getProfile(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null");
        }
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found profile by id = " + id
                ));
    }

    private ProfileDto toDto(ProfileEntity profile) {
        return new ProfileDto(
                profile.getId(),
                profile.getOwner().getId(),
                profile.getPublicName(),
                profile.getAvatarFile() == null ? null : profile.getAvatarFile().getId(),
                profile.getLocation() == null ? null : profile.getLocation().getId(),
                profile.getReputation(),
                profile.getDescription(),
                profile.getUpdateAt(),
                profile.getIsDeleted()
        );
    }
}
