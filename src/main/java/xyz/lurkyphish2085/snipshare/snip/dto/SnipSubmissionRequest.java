package xyz.lurkyphish2085.snipshare.snip.dto;

import java.time.LocalDate;

public record SnipSubmissionRequest(
        String content,
        Boolean isDisposable,
        LocalDate expiryDate
) {
}
