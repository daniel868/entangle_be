package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ent_patient")
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_sequence_generator")
    @SequenceGenerator(name = "patient_sequence_generator", sequenceName = "patient_sequence_generator", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "patient_situation")
    private String patientSituation;

    @Column(name = "patient_contact_info")
    private String patientContactInfo;
}
