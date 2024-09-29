package org.example.entablebe.batch.writer;

import org.example.entablebe.entity.Disease;
import org.example.entablebe.repository.DiseaseRepository;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchWriterConfig {

    private final DiseaseRepository diseaseRepository;

    public BatchWriterConfig(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Bean
    public RepositoryItemWriter<Disease> diseaseRepositoryItemWriter() {
        RepositoryItemWriter<Disease> repositoryItemWriter = new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(diseaseRepository);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }
}
