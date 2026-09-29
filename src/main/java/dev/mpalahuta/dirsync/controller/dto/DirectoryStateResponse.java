package dev.mpalahuta.dirsync.controller.dto;

import dev.mpalahuta.dirsync.service.model.FileInfo;

import java.util.List;

public class DirectoryStateResponse {

    private final List<FileInfo> fileInfoList;

    public DirectoryStateResponse(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

}
