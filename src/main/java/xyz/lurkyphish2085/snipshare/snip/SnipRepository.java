package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SnipRepository extends JpaRepository<Snip, Long> {

    Optional<Snip> findByRetrievalIdAndHasDisposedFalse(String retrievalId);
}
