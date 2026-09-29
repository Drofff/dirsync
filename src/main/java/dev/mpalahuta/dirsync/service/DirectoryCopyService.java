package dev.mpalahuta.dirsync.service;

import dev.mpalahuta.dirsync.service.model.CopyInfo;

import java.util.List;

public interface DirectoryCopyService {
    void createCopy(String serverIp, String localDirPath);
    void deleteCopy(Long id);
    List<CopyInfo> listCopies();

    void refreshCopy(Long id);
}
