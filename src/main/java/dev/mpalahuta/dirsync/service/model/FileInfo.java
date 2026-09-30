package dev.mpalahuta.dirsync.service.model;

import java.time.ZonedDateTime;

public class FileInfo {

    private final String filename;
    private final ZonedDateTime lastModifiedAt;

    public FileInfo(String filename, ZonedDateTime lastModifiedAt) {
        this.filename = filename;
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getFilename() {
        return filename;
    }

    public ZonedDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

}
