package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SnipRepository extends JpaRepository<Snip, Long> {

    default Optional<Snip> findActiveSnipByRetrievelId(String retrievalId) {
        return findByRetrievalIdAndExpiryDateGreaterThanOrExpiryDateIsNull(retrievalId, LocalDate.now());
    }

    @Query("SELECT s FROM Snip s WHERE s.retrievalId = :retrievalId AND (s.expiryDate > :currentDate OR s.expiryDate IS NULL)")
    Optional<Snip> findByRetrievalIdAndExpiryDateGreaterThanOrExpiryDateIsNull(String retrievalId, LocalDate currentDate);

    Optional<Snip> findByRetrievalId(String retrievalId);
    List<Snip> findByAuthor(String author);
    Optional<Snip> findByRetrievalIdAndAuthor(String retrievalId, String author);
    void deleteByRetrievalId(String retrievalId);
    void deleteByRetrievalIdAndAuthor(String retrievalId, String author);
}
