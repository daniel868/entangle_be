package org.example.entablebe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "contact_ent")
@Getter
@Setter
public class ContactEntangle {
    @Id
    @SequenceGenerator(name = "contact_sequence_generator", sequenceName = "contact_sequence_generator", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "contact_sequence_generator",strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "description")
    private String description;

    @Column(name = "contact_method")
    private String contactMethod;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "notification_send")
    private boolean notificationSend;
}
