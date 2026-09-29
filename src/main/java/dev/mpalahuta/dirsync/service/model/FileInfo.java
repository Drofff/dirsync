package dev.mpalahuta.dirsync.service.model;

import java.time.LocalDateTime;

public class FileInfo {

    private final String filename;
    private final LocalDateTime lastModifiedAt;

    public FileInfo(String filename, LocalDateTime lastModifiedAt) {
        this.filename = filename;
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getFilename() {
        return filename;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

}
