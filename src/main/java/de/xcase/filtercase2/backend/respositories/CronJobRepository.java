package de.xcase.filtercase2.backend.respositories;

import de.xcase.filtercase2.backend.entities.CronJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobRepository extends JpaRepository<CronJob, Long> {


}
