package de.xcase.filtercase2.backend.respositories;

import de.xcase.filtercase2.backend.entities.CronJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronJobRepository extends JpaRepository<CronJob, Long> {


}
