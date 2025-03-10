package org.example.entablebe.pojo.medical;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PatientDto {
    private String patientSituation;
    private String patientName;
    private String patientContactInfo;
}
