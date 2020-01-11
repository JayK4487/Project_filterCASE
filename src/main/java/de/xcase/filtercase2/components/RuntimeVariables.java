package de.xcase.filtercase2.components;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDateTime;

@Component
@ApplicationScope
public class RuntimeVariables {

    LocalDateTime lastRun;
    LocalDateTime lastManualRun;
    int totalMails;
    int deletedMails;
    int distributedMails;
    int ambiguousMails;

    public LocalDateTime getLastRun() {
        return lastRun;
    }

    public void setLastRun(LocalDateTime lastRun) {
        this.lastRun = lastRun;
    }

    public LocalDateTime getLastManualRun() {
        return lastManualRun;
    }

    public void setLastManualRun(LocalDateTime lastManualRun) {
        this.lastManualRun = lastManualRun;
    }

    public int getTotalMails() {
        return totalMails;
    }

    public void setTotalMails(int totalMails) {
        this.totalMails = totalMails;
    }

    public int getDeletedMails() {
        return deletedMails;
    }

    public void setDeletedMails(int deletedMails) {
        this.deletedMails = deletedMails;
    }

    public int getDistributedMails() {
        return distributedMails;
    }

    public void setDistributedMails(int distributedMails) {
        this.distributedMails = distributedMails;
    }

    public int getAmbiguousMails() {
        return ambiguousMails;
    }

    public void setAmbiguousMails(int ambiguousMails) {
        this.ambiguousMails = ambiguousMails;
    }
}
