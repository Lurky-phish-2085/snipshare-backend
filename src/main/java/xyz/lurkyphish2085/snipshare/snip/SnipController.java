package xyz.lurkyphish2085.snipshare.snip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.lurkyphish2085.snipshare.auth.annotations.CurrentAuthor;
import xyz.lurkyphish2085.snipshare.common.RestEndpoints;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipRetrievalResponse;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = RestEndpoints.SNIP)
public class SnipController {

    private static final Logger logger = LoggerFactory.getLogger(SnipController.class);

    private final SnipService snipService;

    private SnipController(SnipService snipService) {
        this.snipService = snipService;
    }

    @GetMapping(path = "{retrievalId}")
    private ResponseEntity<SnipRetrievalResponse> getSnip(@PathVariable("retrievalId") String retrievalId, @RequestParam(required = false) Optional<Boolean> metadataOnly) {
        SnipDTO retrievedSnip;

        try {
            retrievedSnip = snipService.getSnip(retrievalId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        String content = retrievedSnip.content();

        if (metadataOnly.orElse(false)) {
            content = "";
        }

        if (retrievedSnip.isDisposable() && !metadataOnly.orElse(false)) {
            snipService.deleteSnip(retrievalId);
        }

        SnipRetrievalResponse response = new SnipRetrievalResponse(
                content,
                retrievedSnip.title(),
                retrievedSnip.author(),
                retrievedSnip.isDisposable(),
                retrievedSnip.expires(),
                retrievedSnip.createdAt(),
                retrievedSnip.expiryDate()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    private ResponseEntity<Void> submitSnip(@RequestBody SnipSubmissionRequest request, UriComponentsBuilder uriBuilder, @CurrentAuthor String author) {
        String retrievalId = snipService.submitSnip(request, author);
        URI createdSnipResourceLocation = uriBuilder
                .path("api/v1/snip/{retrievalId}")
                .buildAndExpand(retrievalId)
                .toUri();

        return ResponseEntity
                .created(createdSnipResourceLocation)
                .build();
    }

    @DeleteMapping(path = "{retrievalId}")
    private ResponseEntity<Void> deleteSnip(@PathVariable String retrievalId, @CurrentAuthor String author, Authentication authentication) {
        List<String> authorities = authentication.getAuthorities().stream().map(Objects::toString).toList();

        if (authorities.contains("ROLE_ADMIN")) {
            snipService.deleteSnip(retrievalId);
        } else {
            snipService.deleteSnip(retrievalId, author);
        }

        return ResponseEntity
                .noContent()
                .build();
    }
}
