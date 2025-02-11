package org.example.entablebe.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ContactEvent extends ApplicationEvent {
    private final String patientContactInfo;
    private final String patientSituation;
    private final String patientName;

    public ContactEvent(Object source, String patientContactInfo, String patientSituation, String patientName) {
        super(source);
        this.patientContactInfo = patientContactInfo;
        this.patientSituation = patientSituation;
        this.patientName = patientName;
    }
}
