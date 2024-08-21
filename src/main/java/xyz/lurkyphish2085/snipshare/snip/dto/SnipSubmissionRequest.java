package xyz.lurkyphish2085.snipshare.snip.dto;

import java.time.LocalDate;

public record SnipSubmissionRequest(
        String content,
        String title,
        Boolean isDisposable,
        LocalDate expiryDate
) {
}
