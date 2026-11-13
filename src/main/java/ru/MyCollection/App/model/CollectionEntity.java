package ru.MyCollection.App.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;

import java.time.LocalDate;

@Entity
@Table(name = "collection")
public class CollectionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "owner_user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_collection_user",
                    options = ForeignKeyOptions.CASCADE_UPDATE_RESTRICT_DELETE
            )
    )
    private UserEntity ownerUserId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cover_file_id") // foreign key
    private Long coverFileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    @ColumnDefault("'PUBLIC'")
    private VisibilityStatus visibility = VisibilityStatus.PUBLIC;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted = false;

    public CollectionEntity() {
    }

    public CollectionEntity(
            Long id,
            UserEntity ownerUserId,
            String name,
            String description,
            Long coverFileId,
            VisibilityStatus visibility,
            LocalDate createdAt,
            LocalDate updatedAt,
            Boolean isDeleted
    ) {
        this.id = id;
        this.ownerUserId = ownerUserId;
        this.name = name;
        this.description = description;
        this.coverFileId = coverFileId;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(UserEntity ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCoverFileId() {
        return coverFileId;
    }

    public void setCoverFileId(Long coverFileId) {
        this.coverFileId = coverFileId;
    }

    public VisibilityStatus getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityStatus visibility) {
        this.visibility = visibility;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
