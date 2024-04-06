package com.bitespeed.backendTask.service.impl;

import com.bitespeed.backendTask.entity.Contact;
import com.bitespeed.backendTask.model.ResponseContactInfoModel;
import com.bitespeed.backendTask.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IdentificationService {

    private final Logger log = LoggerFactory.getLogger(IdentificationService.class);

    @Autowired
    private IdentificationOperationServices identificationOperationServices;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ResponseServices responseServices;

    public ResponseContactInfoModel fetchContactInfo(String email, String phoneNumber) {

        try {

            List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

            if (contacts.isEmpty()) {

                return responseServices.getContactInfo(email,phoneNumber);
            } else {
                Contact contact = contactRepository.findByEmailAndPhoneNumber(email, phoneNumber);

                if (contact == null) {
                    log.info("Contact found for email: {} and phone number: {}", email, phoneNumber);
                    List<Contact> fetchedViaEmail = contactRepository.findByEmail(email);
                    log.info("Fetched contacts via email: {}", fetchedViaEmail);
                    List<Contact> fetchedViaPhoneNumber = contactRepository.findByPhoneNumber(phoneNumber);
                    log.info("Fetched contacts via phone number: {}", fetchedViaPhoneNumber);
                    if (fetchedViaEmail.isEmpty()) {
                        userExistViaPhoneNumber(email, fetchedViaPhoneNumber);
                        return responseServices.getContactInfo(email,phoneNumber);
                    }else{
                        userExistViaEmail(phoneNumber,fetchedViaEmail);
                        return responseServices.getContactInfo(email,phoneNumber);
                    }
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while fetching contact information: {}", e.getMessage(), e);
        }
        return responseServices.getContactInfo(email,phoneNumber);
    }

    public void userExistViaPhoneNumber(String email, List<Contact> fetchedViaPhoneNumber) {
        try {
            Contact primaryContact = null;
            Contact secondaryContact = new Contact();

            for (Contact c : fetchedViaPhoneNumber) {
                if ("primary".equals(c.getLinkPrecedence())) {
                    primaryContact = c;
                    break;
                }
            }

            if (primaryContact == null) {
                Long size = (long) fetchedViaPhoneNumber.size();
                primaryContact = fetchedViaPhoneNumber.get((int) (size - 1));
                secondaryContact.setEmail(email);
                secondaryContact.setPhoneNumber(primaryContact.getPhoneNumber());
                secondaryContact.setLinkPrecedence("secondary");
                secondaryContact.setLinkedId(primaryContact.getLinkedId());
                contactRepository.save(secondaryContact);
            }else{
                secondaryContact.setEmail(email);
                secondaryContact.setPhoneNumber(primaryContact.getPhoneNumber());
                secondaryContact.setLinkPrecedence("secondary");
                secondaryContact.setLinkedId(primaryContact.getId());
                contactRepository.save(secondaryContact);
            }


        } catch (Exception e) {
            log.error("An error occurred while processing user existence via phone number: {}", e.getMessage(), e);
        }
    }

    public void userExistViaEmail(String phoneNumber, List<Contact> fetchedViaEmail){
        try {
            Contact primaryContact = null;
            Contact secondaryContact = new Contact();

            for (Contact c : fetchedViaEmail) {
                if ("primary".equals(c.getLinkPrecedence())) {
                    primaryContact = c;
                    break;
                }
            }

            if (primaryContact == null) {
                Long size = (long) fetchedViaEmail.size();
                primaryContact = fetchedViaEmail.get((int) (size - 1));
                secondaryContact.setEmail(primaryContact.getEmail());
                secondaryContact.setPhoneNumber(phoneNumber);
                secondaryContact.setLinkPrecedence("secondary");
                secondaryContact.setLinkedId(primaryContact.getLinkedId());
                contactRepository.save(secondaryContact);
            }else{
                secondaryContact.setEmail(primaryContact.getEmail());
                secondaryContact.setPhoneNumber(phoneNumber);
                secondaryContact.setLinkPrecedence("secondary");
                secondaryContact.setLinkedId(primaryContact.getId());
                contactRepository.save(secondaryContact);
            }


        } catch (Exception e) {
            log.error("An error occurred while processing user existence via phone number: {}", e.getMessage(), e);
        }
    }

    public Object fetchContactInfoViaEmail(String email) {
        //TODO: operations for if user provides only email
        return null;
    }

    public Object fetchContactInfoViaPhoneNumber(String phoneNumber) {
        //TODO: operations for if users provides only phone
        return null;
    }
}
