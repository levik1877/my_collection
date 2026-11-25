package ru.MyCollection.App.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ValidationErrorMessage {
    public static final String EMPTY = "The field must not be empty";
    public static final String NULL = "The field must not be null";
    public static final String BAD_EMAIL = "Email is not valid";
    public static final String OUT_OF_MAX_SIZE = "";
    public static final String OUT_OF_MIN_SIZE = "";
    public static final String OUT_OF_MIN_OR_MAX_SIZE = "";
}
