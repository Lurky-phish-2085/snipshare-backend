package xyz.lurkyphish2085.snipshare.snip;

import xyz.lurkyphish2085.snipshare.snip.dto.SnipDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipPatchDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;

import java.util.List;

public interface SnipService {

    SnipDTO getSnip(String retrievalId);
    List<SnipDTO> getSnipsByAuthor(String author);
    String submitSnip (SnipSubmissionRequest request, String author);
    SnipDTO patchSnip(String retrievalId, SnipPatchDTO partialUpdate, String author);
    void deleteSnip(String retrievalId);
    SnipDTO deleteSnip(String retrievalId, String author);
}