package de.xcase.filtercase2.backend.respositories;

import de.xcase.filtercase2.backend.entities.UsedFolders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedFolderRepository extends JpaRepository<UsedFolders, Long> {
}
