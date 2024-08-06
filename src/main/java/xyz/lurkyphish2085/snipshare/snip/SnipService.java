package xyz.lurkyphish2085.snipshare.snip;

public interface SnipService {

    Snip getSnip(String retrievalId);
    String submitSnip (SnipSubmissionRequest request);
}