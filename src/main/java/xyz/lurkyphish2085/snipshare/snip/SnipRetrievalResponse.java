package xyz.lurkyphish2085.snipshare.snip;

import java.time.LocalDate;

public record SnipRetrievalResponse (
        String content,
        LocalDate createdAt,
        LocalDate expiryDate
) {
}
