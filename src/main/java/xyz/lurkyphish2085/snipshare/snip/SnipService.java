package xyz.lurkyphish2085.snipshare.snip;

import xyz.lurkyphish2085.snipshare.snip.dto.SnipRetrievalResponse;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;

public interface SnipService {

    SnipRetrievalResponse getSnip(String retrievalId);
    String submitSnip (SnipSubmissionRequest request);
}