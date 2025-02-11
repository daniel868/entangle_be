package org.example.entablebe.event;

import org.example.entablebe.service.MedicalService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EntangleEventHandler {
    private final MedicalService medicalService;

    public EntangleEventHandler(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    @EventListener
    @Async("executor")
    public void handleContactEvent(ContactEvent event) {
        medicalService.initializePatientDisease(
                event.getPatientContactInfo(),
                event.getPatientSituation(),
                event.getPatientName()
        );
    }
}
