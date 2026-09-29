package dev.mpalahuta.dirsync.service.model;

public class CopyInfo {

    private final Long id;
    private final String serverIp;
    private final String localDirPath;

    public CopyInfo(Long id, String serverIp, String localDirPath) {
        this.id = id;
        this.serverIp = serverIp;
        this.localDirPath = localDirPath;
    }

    public Long getId() {
        return id;
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getLocalDirPath() {
        return localDirPath;
    }

}
