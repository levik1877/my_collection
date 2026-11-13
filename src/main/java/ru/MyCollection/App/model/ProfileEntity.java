package ru.MyCollection.App.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "public_name")
    private String publicName;

    @Column(name = "avatar_file_id")
    private Long avatarFileId;

    @Column(name = "country")
    private String country;

    @Column(name = "reputation")
    private Integer reputation;

    @Column(name = "description")
    private String description;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public ProfileEntity() {
    }

    public ProfileEntity(
            Long id,
            Long userId,
            String publicName,
            Long avatarFileId,
            String country,
            Integer reputation,
            String description,
            LocalDate updateAt,
            Boolean isDeleted
    ) {
        this.id = id;
        this.userId = userId;
        this.publicName = publicName;
        this.avatarFileId = avatarFileId;
        this.country = country;
        this.reputation = reputation;
        this.description = description;
        this.updateAt = updateAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public Long getAvatarFileId() {
        return avatarFileId;
    }

    public void setAvatarFileId(Long avatarFileId) {
        this.avatarFileId = avatarFileId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}