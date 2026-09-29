package dev.mpalahuta.dirsync.repository.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "directory_copy")
public class DirectoryCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serverIp;

    @Column(nullable = false, unique = true)
    private String localDirPath;

    @OneToMany
    private Set<TrackedFile> trackedFiles;

    protected DirectoryCopy() {
    }

    public DirectoryCopy(String serverIp, String localDirPath) {
        this.serverIp = serverIp;
        this.localDirPath = localDirPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getLocalDirPath() {
        return localDirPath;
    }

    public void setLocalDirPath(String localDirPath) {
        this.localDirPath = localDirPath;
    }

    public Set<TrackedFile> getTrackedFiles() {
        return trackedFiles;
    }

    public void setTrackedFiles(Set<TrackedFile> trackedFiles) {
        this.trackedFiles = trackedFiles;
    }

}
