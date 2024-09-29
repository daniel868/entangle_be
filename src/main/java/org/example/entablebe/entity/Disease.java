package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "ent_disease")
@Getter
@Setter
@Entity
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "disease_sequence_generator")
    @SequenceGenerator(name = "disease_sequence_generator", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

}
