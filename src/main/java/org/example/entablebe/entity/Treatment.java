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


    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.DETACH
            })
    @JoinTable(
            name = "ent_treatment_competence",
            joinColumns = {@JoinColumn(name = "competence_id")},
            inverseJoinColumns = {@JoinColumn(name = "treatment_id")}
    )
    private Set<Competence> competences;

    public void addCompetences(Competence competence) {
        if (competences == null) {
            competences = new HashSet<>();
        }
        competences.add(competence);
    }

}
