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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "disposable", nullable = false)
    private Boolean isDisposable;

    @Transient
    private Boolean doesExpire;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    protected Snip() {
    }

    public Snip(String retrievalId, String fileName, String title, String author, LocalDate expiryDate, Boolean isDisposable) {
        this.retrievalId = retrievalId;
        this.fileName = fileName;
        this.title = title;
        this.author = author;
        this.expiryDate = expiryDate;
        this.isDisposable = isDisposable;
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

    public Boolean getDisposable() {
        return isDisposable;
    }

    public void setDisposable(Boolean disposable) {
        isDisposable = disposable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = (title.isBlank()) ? "Untitled" : title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getDoesExpire() {
        return this.expiryDate != null;
    }
}
