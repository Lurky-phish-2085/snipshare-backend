package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SnipRepository extends JpaRepository<Snip, Long> {

    Optional<Snip> findByRetrievalId(String retrievalId);
    Optional<Snip> findByRetrievalIdAndExpiryDateGreaterThanEqual(String retrievalId, LocalDate date);
}
