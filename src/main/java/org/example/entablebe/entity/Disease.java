package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Table(name = "ent_disease")
@Getter
@Setter
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Disease.fetchAllDiseaseForUser",
                query = "select d from Disease d " +
                        "left join fetch d.treatments t " +
                        "left join  fetch t.competences c " +
                        "where (c.id is not null AND c.userEntangle.id =:userId) or t.id is null"
        )
})
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "disease_sequence_generator")
    @SequenceGenerator(name = "disease_sequence_generator", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinColumn(name = "disease_id")
    private Set<Treatment> treatments;


    public void addTreatment(Treatment treatment) {
        if (treatments == null) {
            treatments = new HashSet<>();
        }
        treatments.add(treatment);
    }
}
