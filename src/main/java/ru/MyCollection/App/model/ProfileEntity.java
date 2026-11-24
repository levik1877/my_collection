package ru.MyCollection.App.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_profile_user",
                    options = ForeignKeyOptions.CASCADE_UPDATE_RESTRICT_DELETE
            )
    )
    private UserEntity userId;

    @Column(name = "public_name", length = 255, nullable = false)
    private String publicName;

    @Column(name = "avatar_file_id") // foreign key
    private Long avatarFileId;

    @Column(name = "country") // foreign key
    private String country;

    @Column(name = "reputation", nullable = false)
    @ColumnDefault("0")
    private Integer reputation = 0;

    @Column(name = "description")
    private String description;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted = false;
}