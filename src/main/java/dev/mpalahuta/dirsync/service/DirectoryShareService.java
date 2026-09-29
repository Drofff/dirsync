package dev.mpalahuta.dirsync.service;

import dev.mpalahuta.dirsync.service.model.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface DirectoryShareService {
    void startSharing(String localDirPath);
    Optional<String> getSharedDir();
    void stopSharing();

    List<FileInfo> viewDirState();
    InputStream fetchFile(String filename) throws IOException;
}
