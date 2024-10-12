package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "ent_competence")
@Entity
@Getter
@Setter
public class Competence {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "competence_sequence_generator")
    @SequenceGenerator(name = "competence_sequence_generator", initialValue = 1, allocationSize = 1)
    private Long id;

//    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private String type;

}