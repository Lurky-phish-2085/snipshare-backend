package xyz.lurkyphish2085.snipshare.snip;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipPatchDTO;
import xyz.lurkyphish2085.snipshare.snip.dto.SnipSubmissionRequest;

import java.util.List;
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
        Snip snip = snipRepository.findActiveSnipByRetrievelId(retrievalId)
                .orElseThrow(() -> new IllegalArgumentException("Snip '" + retrievalId + "' doesn't exist"));

        String snipContent = snipFileRepository.getSnipFileContent(snip.getFileName())
                .orElseThrow(() -> new NoSuchElementException("Snip file '" + retrievalId + "' doesn't exist"));

        return new SnipDTO(
                snipContent,
                snip.getTitle(),
                snip.getAuthor(),
                snip.getDisposable(),
                snip.getDoesExpire(),
                snip.getCreatedAt(),
                snip.getExpiryDate(),
                snip.getRetrievalId());
    }

    public List<SnipDTO> getSnipsByAuthor(String author) {
        return snipRepository.findByAuthor(author)
                .stream()
                .map((e) ->
                        new SnipDTO(
                                snipFileRepository.getSnipFileContent(e.getFileName()).orElseThrow(() -> new NoSuchElementException("Snip file '" + e.getRetrievalId() + "' doesn't exist")),
                                e.getTitle(),
                                e.getAuthor(),
                                e.getDisposable(),
                                e.getDoesExpire(),
                                e.getCreatedAt(),
                                e.getExpiryDate(),
                                e.getRetrievalId()
                        )
                )
                .toList();
    }

    public String submitSnip(SnipSubmissionRequest request, String author) {
        Snip snip = snipRecordGenerator.generate(request.expiryDate(), request.isDisposable(), request.title(), author);
        snipRepository.save(snip);
        snipFileRepository.writeSnipFile(snip.getFileName(), request.content());

        return snip.getRetrievalId();
    }

    public SnipDTO patchSnip(String retrievalId, SnipPatchDTO partialUpdate, String author) {
        if (author.equals("anonymousUser")) {
            throw new IllegalArgumentException("Author name should not be anonymousUser");
        }

        Snip retrievedSnip = snipRepository.findByRetrievalIdAndAuthor(retrievalId, author)
                .orElseThrow(NoSuchElementException::new);

        if (!author.equals(retrievedSnip.getAuthor())) {
            throw new IllegalArgumentException("Author " + author + " doesn't own snip with " + retrievalId + ".");
        }

        if (partialUpdate.content() != null) {
            snipFileRepository.writeSnipFile(retrievedSnip.getFileName(), partialUpdate.content());
            System.out.println("PARTIAL UPDATE RUN");
            System.out.println("CONTENT: " + partialUpdate.content());
        }

        if (partialUpdate.title() != null) {
            retrievedSnip.setTitle(partialUpdate.title());
        }
        if (partialUpdate.expiryDate() != null) {
            retrievedSnip.setExpiryDate(partialUpdate.expiryDate());
        }

        snipRepository.save(retrievedSnip);

        return new SnipDTO(
                snipFileRepository.getSnipFileContent(retrievedSnip.getFileName()).orElseThrow(() -> new NoSuchElementException("Snip file '" + retrievedSnip.getRetrievalId() + "' doesn't exist")),
                retrievedSnip.getTitle(),
                retrievedSnip.getAuthor(),
                retrievedSnip.getDisposable(),
                retrievedSnip.getDoesExpire(),
                retrievedSnip.getCreatedAt(),
                retrievedSnip.getExpiryDate(),
                retrievedSnip.getRetrievalId()
        );
    }

    @Transactional
    public SnipDTO deleteSnip(String retrievalId, String author) {
        if (author.equals("anonymousUser")) {
            throw new IllegalArgumentException("Author name should not be anonymousUser");
        }

        Snip retrievedSnip = snipRepository.findByRetrievalIdAndAuthor(retrievalId, author)
            .orElseThrow(NoSuchElementException::new);

        if (!author.equals(retrievedSnip.getAuthor())) {
            throw new IllegalArgumentException("Author " + author + " doesn't own snip with " + retrievalId + ".");
        }

        snipRepository.deleteByRetrievalIdAndAuthor(retrievalId, author);

        return new SnipDTO(
                snipFileRepository.getSnipFileContent(retrievedSnip.getFileName()).orElseThrow(() -> new NoSuchElementException("Snip file '" + retrievedSnip.getRetrievalId() + "' doesn't exist")),
                retrievedSnip.getTitle(),
                retrievedSnip.getAuthor(),
                retrievedSnip.getDisposable(),
                retrievedSnip.getDoesExpire(),
                retrievedSnip.getCreatedAt(),
                retrievedSnip.getExpiryDate(),
                retrievedSnip.getRetrievalId()
        );
    }

    @Transactional
    public void deleteSnip(String retrievalId) {
        snipRepository.findByRetrievalId(retrievalId)
            .orElseThrow(() -> new IllegalArgumentException("Snip '" + retrievalId + "' doesn't exist"));

        snipRepository.deleteByRetrievalId(retrievalId);
    }
}
