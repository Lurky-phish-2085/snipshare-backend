package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SnipRepository extends JpaRepository<Snip, Long> {

    default Optional<Snip> findActiveSnipByRetrievelId(String retrievalId) {
        return findByRetrievalIdAndExpiryDateGreaterThanEqualOrExpiryDateIsNull(retrievalId, LocalDate.now());
    }

    Optional<Snip> findByRetrievalId(String retrievalId);
    Optional<Snip> findByRetrievalIdAndExpiryDateGreaterThanEqualOrExpiryDateIsNull(String retrievalId, LocalDate date);
    Optional<Snip> findByAuthor(String author);
    Optional<Snip> findByRetrievalIdAndAuthor(String retrievalId, String author);
    void deleteByRetrievalId(String retrievalId);
    void deleteByRetrievalIdAndAuthor(String retrievalId, String author);
}
