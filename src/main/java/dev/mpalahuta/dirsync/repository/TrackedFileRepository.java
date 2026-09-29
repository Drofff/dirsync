package dev.mpalahuta.dirsync.repository;

import dev.mpalahuta.dirsync.repository.model.TrackedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackedFileRepository extends JpaRepository<TrackedFile, Long> {
}
