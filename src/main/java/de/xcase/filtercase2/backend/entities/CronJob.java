package de.xcase.filtercase2.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

@Entity
public class CronJob {

    @Id
    private Long id;

    /**
     * The execution of the cronjob.
     */
    @Column(name = "execution")
    private LocalTime execution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getExecution() {
        return execution;
    }

    public void setExecution(LocalTime execution) {
        this.execution = execution;
    }
}
