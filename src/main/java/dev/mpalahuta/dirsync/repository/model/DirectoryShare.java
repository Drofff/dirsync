package dev.mpalahuta.dirsync.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "directory_share")
public class DirectoryShare {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String localDirPath;

    protected DirectoryShare() {}

    public DirectoryShare(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalDirPath() {
        return localDirPath;
    }

    public void setLocalDirPath(String localDirPath) {
        this.localDirPath = localDirPath;
    }

}
