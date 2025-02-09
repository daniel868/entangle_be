package org.example.entablebe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "contact_ent")
@Getter
@Setter
public class ContactEntangle {
    @Id
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "contact_method")
    private String contactMethod;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "notification_send")
    private boolean notificationSend;
}
