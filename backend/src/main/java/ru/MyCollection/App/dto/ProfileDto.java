package ru.MyCollection.App.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    Long id;
    @NotNull
    @Positive
    Long userId;
    @NotBlank
    @Size(max = 255)
    String publicName;
    @Positive
    Long avatarFileId;
    @Positive
    Long locationId;
    Integer reputation;
    String description;
    LocalDate updateAt;
    Boolean isDeleted;
}
