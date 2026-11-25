package ru.MyCollection.App.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.MyCollection.App.dto.RoleDto;
import ru.MyCollection.App.model.RoleEntity;
import ru.MyCollection.App.repository.RoleRepository;

import javax.management.relation.Role;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    @Transactional(readOnly = true)
    public List<RoleDto> findAllRoles() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public RoleEntity getRoleById(Long id) {
        return getRole(id);
    }

    @Transactional(readOnly = true)
    public RoleEntity getRoleByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found role by name = " + name
                ));
    }

    @Transactional
    public RoleDto createRole(RoleDto roleToCreate) {
        if(repository.findByName(roleToCreate.getName()).isPresent()) {
            throw new IllegalArgumentException("role with name = " + roleToCreate.getName() + "already exists");
        }
        RoleEntity roleToSave = new RoleEntity(
                null,
                roleToCreate.getName(),
                false
        );
        return toDto(repository.save(roleToSave));
    }

    @Transactional
    public RoleDto updateRoleById(
            Long id,
            RoleDto roleToUpdate
    ) {
        RoleEntity roleEntity = getRole(id);
        roleEntity.setName(roleToUpdate.getName());
        return toDto(repository.save(roleEntity));
    }

    @Transactional
    public RoleDto deleteRoleById(Long id) {
        RoleEntity roleEntity = getRole(id);
        roleEntity.setIsDeleted(true);
        return toDto(repository.save(roleEntity));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoleDto finalDeleteRoleById(Long id) {
        RoleEntity roleEntity = getRole(id);
        repository.delete(roleEntity);
        return toDto(roleEntity);
    }

    private RoleEntity getRole(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found role by id = " + id
                ));
    }

    private RoleDto toDto(RoleEntity roleEntity) {
        return new RoleDto(
                roleEntity.getId(),
                roleEntity.getName(),
                roleEntity.getIsDeleted()
        );
    }
}
