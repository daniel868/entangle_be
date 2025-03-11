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
        @NamedQuery(name = "Disease.fetchAllAvailableDisease",
                query = "select d from Disease d " +
                        "left join fetch d.treatments t " +
                        "left join fetch t.items item " +
                        "where lower(d.name) like lower(:searchString) " +
                        "order by d.name asc"
        ),
        @NamedQuery(name = "Disease.fetchDiseaseByIdWithTreatments",
                query = "select d from Disease d " +
                        "left join fetch d.treatments t " +
                        "where d.id =:diseaseId"),
        @NamedQuery(
                name = "Disease.countAllDisease",
                query = "select count(d) from Disease d " +
                        "where lower(d.name) like lower(:searchString) "
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
            CascadeType.PERSIST,
            CascadeType.REMOVE
    })
    @JoinColumn(name = "disease_id", referencedColumnName = "id")
    private Set<Treatment> treatments;

    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id", referencedColumnName = "id")
    private Set<MedicalNote> medicalNotes;

    public void addTreatment(Treatment treatment) {
        if (treatments == null) {
            treatments = new HashSet<>();
        }
        treatments.add(treatment);
    }

    public void addMedicalNote(MedicalNote note) {
        if (medicalNotes == null) {
            medicalNotes = new HashSet<>();
        }
        medicalNotes.add(note);
    }
}
