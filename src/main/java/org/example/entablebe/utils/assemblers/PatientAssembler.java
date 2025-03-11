package org.example.entablebe.utils.assemblers;

import org.example.entablebe.entity.Patient;
import org.example.entablebe.pojo.medical.PatientDto;
import org.springframework.stereotype.Component;

@Component
public class PatientAssembler {

    public PatientDto assemble(Patient patient) {
        return PatientDto.builder()
                .patientContactInfo(patient.getPatientContactInfo())
                .patientName(patient.getPatientName())
                .patientSituation(patient.getPatientSituation())
                .build();
    }

    public Patient assemble(PatientDto patientDto) {
        return new Patient();
    }
}
