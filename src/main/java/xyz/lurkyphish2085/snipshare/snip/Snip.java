package xyz.lurkyphish2085.snipshare.snip;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "snips")
public class Snip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "retrieval_id", unique = true, nullable = false)
    private String retrievalId;

    @Column(name = "file_name", unique = true, nullable = false)
    private String fileName;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Transient
    private String content;

    protected Snip() {
    }

    public Snip(String retrievalId, String fileName, LocalDate expiryDate) {
        this.retrievalId = retrievalId;
        this.fileName = fileName;
        this.expiryDate = expiryDate;
    }

    public Snip(String content, LocalDate createdAt, LocalDate expiryDate) {
        this.content = content;
        this.createdAt = createdAt;
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "Snip{" +
                "id=" + id +
                ", retrievalId='" + retrievalId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", expiryDate=" + expiryDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRetrievalId() {
        return retrievalId;
    }

    public void setRetrievalId(String retrievalId) {
        this.retrievalId = retrievalId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
