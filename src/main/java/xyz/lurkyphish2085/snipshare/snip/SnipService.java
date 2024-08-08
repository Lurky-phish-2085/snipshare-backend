package xyz.lurkyphish2085.snipshare.snip;

import xyz.lurkyphish2085.snipshare.snip.dto.SnipDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;

public interface SnipService {

    SnipDTO getSnip(String retrievalId);
    String submitSnip (SnipSubmissionRequest request);
}