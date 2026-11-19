package ru.MyCollection.App.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.MyCollection.App.dto.Role;
import ru.MyCollection.App.model.RoleEntity;
import ru.MyCollection.App.repository.RoleRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> findAllRoles() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public RoleEntity getRoleById(
            Long id
    ) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found role by id = " + id
                ));
    }

    public RoleEntity getRoleByName(
            String name
    ) {
        return repository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found role by name = " + name
                ));
    }

    private Role toDto(
            RoleEntity roleEntity
    ) {
        return new Role(
                roleEntity.getId(),
                roleEntity.getName(),
                roleEntity.getIsDeleted()
        );
    }
}
