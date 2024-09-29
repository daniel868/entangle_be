package org.example.entablebe.batch.processor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchProcessorConfig {

    @Bean
    public DiseaseProcessor diseaseProcessor() {
        return new DiseaseProcessor();
    }
}
