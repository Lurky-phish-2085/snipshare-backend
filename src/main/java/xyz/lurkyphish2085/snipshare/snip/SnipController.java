package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipRetrievalResponse;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionResponse;

@RestController
@RequestMapping(path = "api/v1/snip")
public class SnipController {

    private final SnipService snipService;

    @Autowired
    public SnipController(SnipService snipService) {
        this.snipService = snipService;
    }

    @GetMapping(path = "{retrievalId}")
    public SnipRetrievalResponse getSnip(@PathVariable("retrievalId") String retrievalId) {
        Snip retrievedSnip =  snipService.getSnip(retrievalId);

        return new SnipRetrievalResponse(
                retrievedSnip.getContent(),
                retrievedSnip.getCreatedAt(),
                retrievedSnip.getExpiryDate()
        );
    }

    @PostMapping
    public SnipSubmissionResponse submitSnip(@RequestBody SnipSubmissionRequest request) {
        String retrievalId = snipService.submitSnip(request);

        return new SnipSubmissionResponse(retrievalId);
    }
}
