package ru.MyCollection.App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.MyCollection.App.model.UserEntity;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    Long id;
    UserEntity userId;
    String publicName;
    Long avatarFileId;
    String country;
    Integer reputation;
    String description;
    LocalDate updateAt;
    Boolean isDeleted;
}
