package xyz.lurkyphish2085.snipshare.snip;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;

import java.time.LocalDate;
import java.util.NoSuchElementException;

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
        Snip snip = snipRepository.findByRetrievalIdAndExpiryDateGreaterThanEqual(retrievalId, LocalDate.now())
                .orElseThrow(() -> new IllegalArgumentException("Snip '" + retrievalId + "' doesn't exist"));

        String snipContent = snipFileRepository.getSnipFileContent(snip.getFileName())
                .orElseThrow(() -> new NoSuchElementException("Snip file '" + retrievalId + "' doesn't exist"));

        return new SnipDTO(snipContent, snip.getTitle(), snip.getAuthor(), snip.getDisposable(), snip.getCreatedAt(), snip.getExpiryDate());
    }

    public String submitSnip(SnipSubmissionRequest request, String author) {
        Snip snip = snipRecordGenerator.generate(request.expiryDate(), request.isDisposable(), request.title(), author);
        snipRepository.save(snip);
        snipFileRepository.writeSnipFile(snip.getFileName(), request.content());

        return snip.getRetrievalId();
    }

    @Transactional
    public void deleteSnip(String retrievalId, String author) {
        if (author.equals("anonymousUser")) {
            throw new IllegalArgumentException("Author name should not be anonymousUser");
        }

        snipRepository.findByRetrievalIdAndAuthor(retrievalId, author)
            .orElseThrow(NoSuchElementException::new);

        snipRepository.deleteByRetrievalIdAndAuthor(retrievalId, author);
    }

    @Transactional
    public void deleteSnip(String retrievalId) {
        snipRepository.findByRetrievalId(retrievalId)
            .orElseThrow(() -> new IllegalArgumentException("Snip '" + retrievalId + "' doesn't exist"));

        snipRepository.deleteByRetrievalId(retrievalId);
    }
}
