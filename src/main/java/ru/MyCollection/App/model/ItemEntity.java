package ru.MyCollection.App.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import ru.MyCollection.App.model.entityEnums.ItemStatus;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;

import java.time.OffsetDateTime;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private UserEntity user;

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
}
