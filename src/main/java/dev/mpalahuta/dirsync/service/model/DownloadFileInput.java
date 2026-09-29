package dev.mpalahuta.dirsync.service.model;

public class DownloadFileInput {

    private final String serverIp;
    private final String remoteFilename;
    private final String localFilePath;

    public DownloadFileInput(String serverIp, String remoteFilename, String localFilePath) {
        this.serverIp = serverIp;
        this.remoteFilename = remoteFilename;
        this.localFilePath = localFilePath;
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getRemoteFilename() {
        return remoteFilename;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

}
