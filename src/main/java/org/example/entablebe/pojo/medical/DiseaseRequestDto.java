package org.example.entablebe.pojo.medical;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DiseaseRequestDto {
    private String diseaseName;
    private List<TreatmentItemDto> items;
}
