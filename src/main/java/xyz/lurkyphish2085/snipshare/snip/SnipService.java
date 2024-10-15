package xyz.lurkyphish2085.snipshare.snip;

import xyz.lurkyphish2085.snipshare.snip.dto.SnipDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;

public interface SnipService {

    SnipDTO getSnip(String retrievalId);
    String submitSnip (SnipSubmissionRequest request, String author);
    void deleteSnip(String retrievalId);
    void deleteSnip(String retrievalId, String author);
}