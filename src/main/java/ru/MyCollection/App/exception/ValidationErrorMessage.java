package ru.MyCollection.App.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ValidationErrorMessage {
    public static final String NOT_EMPTY = "The field must not be empty";
    public static final String BAD_EMAIL = "Email is not valid";
}
