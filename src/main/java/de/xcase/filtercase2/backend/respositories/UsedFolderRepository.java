package de.xcase.filtercase2.backend.respositories;

import de.xcase.filtercase2.backend.entities.UsedFolders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsedFolderRepository extends JpaRepository<UsedFolders, Long> {
}
