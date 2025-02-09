package org.example.entablebe.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ContactEvent extends ApplicationEvent {
    private final String patientContactInfo;
    private final String patientSituation;
    public ContactEvent(Object source, String patientSituation, String patientContactInfo) {
        super(source);
        this.patientContactInfo = patientSituation;
        this.patientSituation = patientContactInfo;
    }
}
