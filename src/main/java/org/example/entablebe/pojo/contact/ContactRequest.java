package org.example.entablebe.pojo.contact;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContactRequest {
    private String patientSituation;
    private String patientContactInfo;
    private String patientName;
}
