package dev.mpalahuta.dirsync.service;

import dev.mpalahuta.dirsync.service.model.DownloadFileInput;
import dev.mpalahuta.dirsync.service.model.FileInfo;

import java.util.List;

public interface RemoteDirectoryClient {
    List<FileInfo> viewDirState(String serverIp);
    void downloadFile(DownloadFileInput input);
}
