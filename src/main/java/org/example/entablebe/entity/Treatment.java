package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.DETACH
    })
    @JoinColumn(name = "user_id")
    private UserEntangle user;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REMOVE}
    )
    @JoinColumn(name = "treatment_id")
    private Set<TreatmentItem> items;

    public void addTreatmentItem(TreatmentItem item) {
        if (items == null) {
            items = new HashSet<>();
        }
        items.add(item);
    }
}
