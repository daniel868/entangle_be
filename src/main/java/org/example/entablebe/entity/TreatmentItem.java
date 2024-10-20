package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ent_treatment_item")
public class TreatmentItem {

    @Id
    @SequenceGenerator(name = "treatment_item_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treatment_item_sequence")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;
}