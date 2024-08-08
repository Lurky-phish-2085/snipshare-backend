package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;

import java.util.Optional;

@Service
public class SnipServiceImpl implements SnipService {

    private final SnipRepository snipRepository;
    private final SnipFileRepository snipFileRepository;
    private final SnipRecordGenerator snipRecordGenerator;

    @Autowired
    public SnipServiceImpl(SnipRepository snipRepository, SnipFileRepository snipFileRepository, SnipRecordGenerator snipRecordGenerator) {
        this.snipRepository = snipRepository;
        this.snipFileRepository = snipFileRepository;
        this.snipRecordGenerator = snipRecordGenerator;
    }

    public SnipDTO getSnip(String retrievalId) {
        Optional<Snip> snipOptional = snipRepository.findByRetrievalId(retrievalId);
        if (snipOptional.isEmpty()) {
            throw new IllegalStateException("Snip '" + retrievalId + "' doesn't exist");
        }
        Snip snip = snipOptional.get();

        Optional<String> snipContentOptional = snipFileRepository.getSnipFileContent(snip.getFileName());
        if (snipContentOptional.isEmpty()) {
            throw new IllegalStateException("Snip file '" + retrievalId + "' doesn't exist");
        }
        String snipContent = snipContentOptional.get();


        return new SnipDTO(snipContent, snip.getCreatedAt(), snip.getExpiryDate());
    }

    public String submitSnip(SnipSubmissionRequest request) {
        Snip snip = snipRecordGenerator.generate(request.expiryDate());
        snipRepository.save(snip);
        snipFileRepository.writeSnipFile(snip.getFileName(), request.content());

        return snip.getRetrievalId();
    }
}
