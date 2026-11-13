package ru.MyCollection.App.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name = "\"user\"")
@Entity
public class UserEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id ")
    private Long role_id;

    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "email")
    private String email;

    @Column(name = "email_verified_at")
    private LocalDate emailVerifiedAt;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updateAt;

    @Column(name = "last_login_at")
    private LocalDate lastLoginAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public UserEntity() {}
    public UserEntity(
            Long id,
            Long role_id,
            String username,
            String passwordHash,
            String email,
            LocalDate emailVerifiedAt,
            LocalDate createdAt,
            LocalDate updateAt,
            LocalDate lastLoginAt,
            Boolean isDeleted
    ) {
        this.id = id;
        this.role_id = role_id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.emailVerifiedAt = emailVerifiedAt;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.lastLoginAt = lastLoginAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(LocalDate emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDate getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDate lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
