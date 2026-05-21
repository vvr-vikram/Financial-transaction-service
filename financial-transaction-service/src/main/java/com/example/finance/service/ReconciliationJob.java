package com.example.finance.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReconciliationJob {

   
    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailyReconciliation() {
        log.info("Starting daily reconciliation job...");
        log.info("Daily reconciliation job completed.");
    }
}
