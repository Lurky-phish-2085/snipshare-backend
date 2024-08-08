package xyz.lurkyphish2085.snipshare.snip.dto;

import java.time.LocalDate;

public record SnipDTO(
        String content,
        LocalDate createdAt,
        LocalDate expiryDate
) {
}
