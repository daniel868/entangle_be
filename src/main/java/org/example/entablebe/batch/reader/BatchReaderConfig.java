package org.example.entablebe.batch.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class BatchReaderConfig {

    @Bean
    public FlatFileItemReader<String> diseaseLineReader() {
        FlatFileItemReader<String> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/import/diseases_mock.csv"));
        flatFileItemReader.setName("diseaseLineReader");
        flatFileItemReader.setLineMapper(diseaseLineMapper());
        return flatFileItemReader;
    }


    private LineMapper<String> diseaseLineMapper() {
        return (line, lineNumber) -> line;
    }


}
