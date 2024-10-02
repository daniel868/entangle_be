package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Table(name = "ent_treatment")
@Entity
@Getter
@Setter
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treatment_sequence_generator")
    @SequenceGenerator(name = "treatment_sequence_generator", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "treatment_type")
    private String treatmentType;

}
