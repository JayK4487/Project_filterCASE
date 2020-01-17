package de.xcase.filtercase2.backend.respositories;

import de.xcase.filtercase2.backend.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    //Folder findBySourceFolder(String sourceFolder);
    Folder findByDestinationFolder(String destinationFolder);
}
