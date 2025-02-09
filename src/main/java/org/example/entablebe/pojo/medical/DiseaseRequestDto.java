package org.example.entablebe.pojo.medical;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DiseaseRequestDto {
    private String diseaseName;
    private List<TreatmentItemDto> items;
}
