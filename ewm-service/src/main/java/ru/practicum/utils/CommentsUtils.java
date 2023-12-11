package ru.practicum.utils;

import java.time.LocalDateTime;

public class CommentsUtils {
    public static LocalDateTime getDefaultCommentCreatedDate() {
        return LocalDateTime.now();
    }
}