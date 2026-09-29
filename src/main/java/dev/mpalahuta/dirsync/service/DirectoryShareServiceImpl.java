package dev.mpalahuta.dirsync.service;

import dev.mpalahuta.dirsync.repository.DirectoryShareRepository;
import dev.mpalahuta.dirsync.repository.model.DirectoryShare;
import dev.mpalahuta.dirsync.service.model.FileInfo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class DirectoryShareServiceImpl implements DirectoryShareService {

    private static final Long SINGLETON_ENTITY_ID = 1L;

    private final DirectoryShareRepository directoryShareRepository;

    public DirectoryShareServiceImpl(DirectoryShareRepository directoryShareRepository) {
        this.directoryShareRepository = directoryShareRepository;
    }

    @Override
    @Transactional
    public void startSharing(String localDirPath) {
        if (!Files.exists(Path.of(localDirPath))) {
            throw new IllegalArgumentException("invalid path: not exists");
        }

        var existingRowOpt = this.directoryShareRepository.findById(SINGLETON_ENTITY_ID);
        DirectoryShare row = existingRowOpt.orElseGet(() -> new DirectoryShare(SINGLETON_ENTITY_ID));
        row.setLocalDirPath(localDirPath);
        this.directoryShareRepository.save(row);
    }

    @Override
    @Transactional
    public Optional<String> getSharedDir() {
        return this.directoryShareRepository.findById(SINGLETON_ENTITY_ID)
                .map(DirectoryShare::getLocalDirPath);
    }

    @Override
    @Transactional
    public void stopSharing() {
        this.directoryShareRepository.deleteById(SINGLETON_ENTITY_ID);
    }

    private FileInfo resolveFileInfo(Path filePath) {
        final String filename = filePath.getFileName().toString();

        LocalDateTime lastModifiedAt;
        try {
            final var lastModifiedInstant = Files.getLastModifiedTime(filePath).toInstant();
            lastModifiedAt = LocalDateTime.ofInstant(lastModifiedInstant, ZoneId.systemDefault());
        } catch (IOException e) {
            throw new RuntimeException("read last modified time of " + filePath, e);
        }

        return new FileInfo(filename, lastModifiedAt);
    }

    @Override
    @Transactional
    public List<FileInfo> viewDirState() {
        final Optional<String> dirPathOpt = this.getSharedDir();
        if (dirPathOpt.isEmpty()) {
            throw new IllegalStateException("no directory is being shared");
        }

        final var dirPath = Path.of(dirPathOpt.get());

        try (final var filesStream = Files.list(dirPath)) {
            return filesStream.map(this::resolveFileInfo).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public InputStream fetchFile(String filename) throws IOException {
        final Optional<String> dirPathOpt = this.getSharedDir();
        if (dirPathOpt.isEmpty()) {
            throw new IllegalStateException("no directory is being shared");
        }

        final var filePath = Path.of(dirPathOpt.get(), filename);
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("file does not exist");
        }

        return Files.newInputStream(filePath);
    }

}
