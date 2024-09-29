package org.example.entablebe.batch.job;

import org.example.entablebe.batch.processor.DiseaseProcessor;
import org.example.entablebe.entity.Disease;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final FlatFileItemReader<String> diseaseLineReader;
    private final DiseaseProcessor diseaseProcessor;
    private final RepositoryItemWriter<Disease> diseaseRepositoryItemWriter;

    @Value("${spring.batch.size}")
    private int batchSize;

    public BatchJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<String> diseaseLineReader, DiseaseProcessor diseaseProcessor, RepositoryItemWriter<Disease> diseaseRepositoryItemWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.diseaseLineReader = diseaseLineReader;
        this.diseaseProcessor = diseaseProcessor;
        this.diseaseRepositoryItemWriter = diseaseRepositoryItemWriter;
    }

    @Bean
    public Step importDiseaseStep() {
        return new StepBuilder("importDiseaseStep", jobRepository)
                .<String, Disease>chunk(batchSize, transactionManager)
                .reader(diseaseLineReader)
                .processor(diseaseProcessor)
                .writer(diseaseRepositoryItemWriter)
                .build();
    }

    @Bean
    public Job diseaseImportJob() {
        return new JobBuilder("diseaseImportJob", jobRepository)
                .start(importDiseaseStep())
                .build();
    }
}
