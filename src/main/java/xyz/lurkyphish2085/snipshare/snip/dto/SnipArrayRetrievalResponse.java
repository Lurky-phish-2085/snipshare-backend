package xyz.lurkyphish2085.snipshare.snip.dto;

import java.util.List;

public record SnipArrayRetrievalResponse(
        List<SnipDTO> snips
) {
}
