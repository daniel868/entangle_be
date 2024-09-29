package org.example.entablebe.batch.processor;

import org.example.entablebe.entity.Disease;
import org.springframework.batch.item.ItemProcessor;


public class DiseaseProcessor implements ItemProcessor<String, Disease> {

    @Override
    public Disease process(String item) throws Exception {
        Disease disease = new Disease();
        disease.setName(item);
        return disease;
    }
}
