package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Table(name = "ent_medical_note")
@Getter
@Setter
@Entity
public class MedicalNote {
    @Id
    @SequenceGenerator(name = "medical_note_sequence_generator", sequenceName = "medical_note_sequence_generator", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "medical_note_sequence_generator")
    private Long id;

    @Column(name = "note_content")
    private String noteContent;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntangle createdBy;

}
