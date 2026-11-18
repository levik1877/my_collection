package ru.MyCollection.App.dto;

public record UserToCreate (
    String username,
    String passwordHash,
    String email
) {

}
