package org.example.entablebe.pojo.medical;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DiseaseDto {
    private String diseaseName;
    private Map<String, TreatmentDto> treatments;
}
