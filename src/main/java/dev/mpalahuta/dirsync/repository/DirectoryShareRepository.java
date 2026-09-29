package dev.mpalahuta.dirsync.repository;

import dev.mpalahuta.dirsync.repository.model.DirectoryShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectoryShareRepository extends JpaRepository<DirectoryShare, Long> {
}
