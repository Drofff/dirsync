package dev.mpalahuta.dirsync.repository.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracked_file")
public class TrackedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @ManyToOne
    private DirectoryCopy parentCopy;

    protected TrackedFile() {}

    public TrackedFile(String filename, LocalDateTime lastModifiedAt, DirectoryCopy parentCopy) {
        this.filename = filename;
        this.lastModifiedAt = lastModifiedAt;
        this.parentCopy = parentCopy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public DirectoryCopy getParentCopy() {
        return parentCopy;
    }

    public void setParentCopy(DirectoryCopy parentCopy) {
        this.parentCopy = parentCopy;
    }

}
