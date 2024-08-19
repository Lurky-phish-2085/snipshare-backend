package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipRetrievalResponse;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionResponse;

import java.net.URI;

@RestController
@RequestMapping(path = "api/v1/snip")
public class SnipController {

    private final SnipService snipService;

    private SnipController(SnipService snipService) {
        this.snipService = snipService;
    }

    @GetMapping(path = "{retrievalId}")
    private ResponseEntity<SnipRetrievalResponse> getSnip(@PathVariable("retrievalId") String retrievalId) {
        SnipDTO retrievedSnip;

        try {
            retrievedSnip = snipService.getSnip(retrievalId);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        SnipRetrievalResponse response = new SnipRetrievalResponse(
                retrievedSnip.content(),
                retrievedSnip.isDisposable(),
                retrievedSnip.createdAt(),
                retrievedSnip.expiryDate()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    private ResponseEntity<SnipSubmissionResponse> submitSnip(@RequestBody SnipSubmissionRequest request, UriComponentsBuilder uriBuilder) {
        String retrievalId = snipService.submitSnip(request);
        URI createdSnipResourceLocation = uriBuilder
                .path("api/v1/snip/{retrievalId}")
                .buildAndExpand(retrievalId)
                .toUri();

        return ResponseEntity
                .created(createdSnipResourceLocation)
                .build();
    }
}
