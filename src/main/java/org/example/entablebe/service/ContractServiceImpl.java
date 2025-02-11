package org.example.entablebe.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entablebe.entity.ContactEntangle;
import org.example.entablebe.event.ContactEvent;
import org.example.entablebe.pojo.contact.ContactRequest;
import org.example.entablebe.repository.ContactRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ContractServiceImpl implements ContactService {
    private static final Logger logger = LogManager.getLogger(ContractServiceImpl.class);

    private final ContactRepository contactRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ContractServiceImpl(ContactRepository contactRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.contactRepository = contactRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    @Transactional
    public boolean addNewContact(ContactRequest contactRequest) {
        try {

            ContactEntangle contactEntangle = new ContactEntangle();
            contactEntangle.setContactMethod(contactRequest.getPatientContactInfo());
            contactEntangle.setDescription(contactRequest.getPatientSituation());
            contactEntangle.setCreatedDate(new Date());
            contactEntangle.setContactName(contactRequest.getPatientName());
            contactRepository.save(contactEntangle);

            ContactEvent event = new ContactEvent(this,
                    contactRequest.getPatientContactInfo(),
                    contactRequest.getPatientSituation(),
                    contactRequest.getPatientName());
            applicationEventPublisher.publishEvent(event);
            return true;

        } catch (Exception e) {
            logger.debug("error inserting new contact into db ", e);
            return false;
        }
    }
}
