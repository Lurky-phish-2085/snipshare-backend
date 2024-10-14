package xyz.lurkyphish2085.snipshare.snip.dto;

import java.time.LocalDate;

public record SnipRetrievalResponse (
        String content,
        String title,
        String author,
        Boolean isDisposable,
        LocalDate createdAt,
        LocalDate expiryDate
) {
}
