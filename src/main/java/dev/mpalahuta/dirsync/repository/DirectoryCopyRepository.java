package dev.mpalahuta.dirsync.repository;

import dev.mpalahuta.dirsync.repository.model.DirectoryCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectoryCopyRepository extends JpaRepository<DirectoryCopy, Long> {
}
