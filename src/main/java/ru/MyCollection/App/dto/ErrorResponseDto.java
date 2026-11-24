package ru.MyCollection.App.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto (
    String message,
    String detailedMessage,
    LocalDateTime errorTime
) {

}
