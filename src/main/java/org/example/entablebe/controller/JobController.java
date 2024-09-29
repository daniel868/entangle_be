package org.example.entablebe.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {
    private final static Logger logger = LogManager.getLogger(JobController.class);

    private final JobLauncher jobLauncher;
    private final Job diseaseImportJob;

    public JobController(JobLauncher jobLauncher, Job diseaseImportJob) {
        this.jobLauncher = jobLauncher;
        this.diseaseImportJob = diseaseImportJob;
    }

    @RequestMapping(value = "/importDisease", method = RequestMethod.POST)
    public void triggerDiseaseImportJob() {
        try {
            jobLauncher.run(diseaseImportJob, new JobParameters());
        } catch (Exception e) {
            logger.error("triggerDiseaseImportJob - error", e);
        }
    }
}
