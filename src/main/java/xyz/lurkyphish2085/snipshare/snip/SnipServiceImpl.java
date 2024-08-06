package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SnipServiceImpl implements SnipService {

    private final SnipRepository snipRepository;
    private final SnipRecordGenerator snipRecordGenerator;

    @Autowired
    public SnipServiceImpl(SnipRepository snipRepository, SnipRecordGenerator snipRecordGenerator) {
        this.snipRepository = snipRepository;
        this.snipRecordGenerator = snipRecordGenerator;
    }

    public Snip getSnip(String retrievalId) {
        Optional<Snip> snipOptional = snipRepository.findByRetrievalId(retrievalId);
        if (snipOptional.isEmpty()) {
            throw new IllegalStateException("Snip '" + retrievalId + "' doesn't exist");
        }

        return snipOptional.get();
    }

    public String submitSnip(SnipSubmissionRequest request) {
        Snip snip = snipRecordGenerator.generate(request.expiryDate());
        snipRepository.save(snip);

        return snip.getRetrievalId();
    }
}
