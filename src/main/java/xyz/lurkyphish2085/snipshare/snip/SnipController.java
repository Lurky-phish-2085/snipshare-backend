package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/snip")
public class SnipController {

    private final SnipService snipService;

    @Autowired
    public SnipController(SnipService snipService) {
        this.snipService = snipService;
    }

    @GetMapping(path = "{retrievalId}")
    public Snip getSnip(@PathVariable("retrievalId") String retrievalId) {
        return snipService.getSnip(retrievalId);
    }

    @PostMapping
    public SnipSubmissionResponse submitSnip(@RequestBody SnipSubmissionRequest request) {
        String retrievalId = snipService.submitSnip(request);

        return new SnipSubmissionResponse(retrievalId);
    }
}