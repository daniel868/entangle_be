package org.example.entablebe.pojo.medical;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiseaseTreatmentResponse {
    private List<DiseaseDto> diseaseDto;
}
