package xyz.lurkyphish2085.snipshare.snip.dto;

import java.time.LocalDate;

public record SnipPatchDTO(
        String title,
        String content,
        LocalDate expiryDate
) {
}
