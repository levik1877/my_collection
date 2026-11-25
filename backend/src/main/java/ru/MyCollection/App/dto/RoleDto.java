package ru.MyCollection.App.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    Long id;
    @NotBlank
    @Size(max = 50)
    String name;
    Boolean isDeleted;
}
