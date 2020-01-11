package de.xcase.filtercase2.backend.respositories;

import de.xcase.filtercase2.backend.entities.EMailAdresses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EMailAdressesRespository extends JpaRepository<EMailAdresses, Long> {
}
