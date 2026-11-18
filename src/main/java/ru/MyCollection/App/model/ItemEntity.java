package ru.MyCollection.App.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import ru.MyCollection.App.model.entityEnums.ItemStatus;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;

import java.time.OffsetDateTime;

@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "collection_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_collection_item",
                    options = ForeignKeyOptions.CASCADE_UPDATE_RESTRICT_DELETE
            )
    )
    private CollectionEntity collectionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "owner_user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_item_user",
                    options = ForeignKeyOptions.CASCADE_UPDATE_RESTRICT_DELETE
            )
    )
    private UserEntity ownerUserId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    @ColumnDefault("'PUBLIC'")
    private VisibilityStatus visibility = VisibilityStatus.PUBLIC;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @ColumnDefault("'PENDING'")
    private ItemStatus status = ItemStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted = false;

    public ItemEntity() {
    }

    public ItemEntity(
            Long id,
            CollectionEntity collectionId,
            UserEntity ownerUserId,
            String name,
            String description,
            VisibilityStatus visibility,
            ItemStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            Boolean isDeleted
    ) {
        this.id = id;
        this.collectionId = collectionId;
        this.ownerUserId = ownerUserId;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.status = status;
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

    public CollectionEntity getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(CollectionEntity collectionId) {
        this.collectionId = collectionId;
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

    public VisibilityStatus getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityStatus visibility) {
        this.visibility = visibility;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
