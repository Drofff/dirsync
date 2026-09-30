package dev.mpalahuta.dirsync.service;

import dev.mpalahuta.dirsync.repository.DirectoryCopyRepository;
import dev.mpalahuta.dirsync.repository.TrackedFileRepository;
import dev.mpalahuta.dirsync.repository.model.DirectoryCopy;
import dev.mpalahuta.dirsync.repository.model.TrackedFile;
import dev.mpalahuta.dirsync.service.model.CopyInfo;
import dev.mpalahuta.dirsync.service.model.DownloadFileInput;
import dev.mpalahuta.dirsync.service.model.FileInfo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DirectoryCopyServiceImpl implements DirectoryCopyService {

    private static final Logger log = LoggerFactory.getLogger(DirectoryCopyServiceImpl.class);

    private final DirectoryCopyRepository directoryCopyRepository;
    private final TrackedFileRepository trackedFileRepository;
    private final RemoteDirectoryClient remoteDirectoryClient;

    public DirectoryCopyServiceImpl(DirectoryCopyRepository directoryCopyRepository,
                                    TrackedFileRepository trackedFileRepository,
                                    RemoteDirectoryClient remoteDirectoryClient) {
        this.directoryCopyRepository = directoryCopyRepository;
        this.trackedFileRepository = trackedFileRepository;
        this.remoteDirectoryClient = remoteDirectoryClient;
    }

    @Override
    @Transactional
    public void createCopy(String serverIp, String localDirPath) {
        final var dirPath = Path.of(localDirPath);
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("directory does not exist");
        }
        try (final var files = Files.list(dirPath)) {
            if (!files.toList().isEmpty()) {
                throw new IllegalArgumentException("local directory must be empty (it is for your safety)");
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to list local dir", e);
        }

        try {
            final var directoryCopy = new DirectoryCopy(serverIp, localDirPath);
            this.directoryCopyRepository.save(directoryCopy);
        } catch (Exception _) {
            throw new IllegalArgumentException("duplicate usage of directory or server");
        }
    }

    @Override
    @Transactional
    public void deleteCopy(Long id) {
        this.directoryCopyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<CopyInfo> listCopies() {
        return this.directoryCopyRepository.findAll().stream()
                .map(dc -> new CopyInfo(dc.getId(), dc.getServerIp(), dc.getLocalDirPath()))
                .toList();
    }

    private void removeTrackedFile(TrackedFile trackedFile) {
        try {
            Files.delete(Path.of(trackedFile.getFilename()));
        } catch (IOException e) {
            throw new RuntimeException("failed to delete file locally", e);
        }

        this.trackedFileRepository.deleteById(trackedFile.getId());

        log.info("removed file: {}", trackedFile.getFilename());
    }

    private void fetchRemoteFile(String fileToFetch, DirectoryCopy dirCopy) {
        final var downloadInput = new DownloadFileInput(dirCopy.getServerIp(), fileToFetch, dirCopy.getLocalDirPath());
        this.remoteDirectoryClient.downloadFile(downloadInput);

        final var trackedFile = new TrackedFile(fileToFetch, LocalDateTime.now(), dirCopy);
        this.trackedFileRepository.saveAndFlush(trackedFile);

        log.info("downloaded file: {}", fileToFetch);
    }

    @Override
    @Transactional
    public void refreshCopy(Long id) {
        final var dirCopy = this.directoryCopyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("unknown copy id"));

        log.info("starting refresh for server {}", dirCopy.getServerIp());

        final Map<String, TrackedFile> trackedFilesMap = new HashMap<>();
        final List<TrackedFile> trackedFiles = this.trackedFileRepository.findByParentCopy(dirCopy);
        trackedFiles.forEach(trackedFile -> trackedFilesMap.put(trackedFile.getFilename(), trackedFile));

        log.info("number of files tracked locally: {}", trackedFilesMap.size());

        final List<FileInfo> remoteFiles = this.remoteDirectoryClient.viewDirState(dirCopy.getServerIp());

        log.info("number of files in remote dir: {}", remoteFiles.size());

        final Set<String> remoteFilenames = new HashSet<>();

        final List<String> filesToFetch = new LinkedList<>();
        remoteFiles.forEach(rf -> {
            remoteFilenames.add(rf.getFilename());

            final var trackedFile = trackedFilesMap.get(rf.getFilename());
            if (trackedFile == null) {
                filesToFetch.add(rf.getFilename());
                return;
            }

            if (trackedFile.getLastModifiedAt().isBefore(rf.getLastModifiedAt())) {
                removeTrackedFile(trackedFile);
                filesToFetch.add(rf.getFilename());
            }
        });

        trackedFiles.stream()
                .filter(tf -> !remoteFilenames.contains(tf.getFilename()))
                .forEach(this::removeTrackedFile);

        filesToFetch.forEach(fileToFetch -> fetchRemoteFile(fileToFetch, dirCopy));
    }

}
