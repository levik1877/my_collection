package ru.MyCollection.App.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import ru.MyCollection.App.model.entityEnums.VisibilityStatus;

import java.time.LocalDate;

@Entity
@Table(name = "collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private UserEntity user;

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
}
