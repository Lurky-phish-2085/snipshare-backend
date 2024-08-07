package xyz.lurkyphish2085.snipshare.snip.dto;

import java.time.LocalDate;

public record SnipRetrievalResponse (
        String content,
        LocalDate createdAt,
        LocalDate expiryDate
) {
}
