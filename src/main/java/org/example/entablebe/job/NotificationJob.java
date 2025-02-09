package org.example.entablebe.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class NotificationJob {

    private final TransactionTemplate transactionTemplate;

    public NotificationJob(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Scheduled(cron = "0 0 * * * 1-5")
    public void notifyAvailableContact() {
        //TODO: notify via email in transaction
        transactionTemplate.executeWithoutResult((status) -> {

        });
    }
}
