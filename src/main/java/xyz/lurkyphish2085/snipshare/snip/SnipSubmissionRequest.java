package xyz.lurkyphish2085.snipshare.snip;

import java.time.LocalDate;

public record SnipSubmissionRequest(
        String content,
        LocalDate expiryDate
) {
}
