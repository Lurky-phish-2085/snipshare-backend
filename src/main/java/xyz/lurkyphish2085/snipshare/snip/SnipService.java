package xyz.lurkyphish2085.snipshare.snip;

public interface SnipService {

    SnipRetrievalResponse getSnip(String retrievalId);
    String submitSnip (SnipSubmissionRequest request);
}