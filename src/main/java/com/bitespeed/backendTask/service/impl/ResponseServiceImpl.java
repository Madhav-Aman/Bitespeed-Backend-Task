package com.bitespeed.backendTask.service.impl;

import com.bitespeed.backendTask.entity.Contact;
import com.bitespeed.backendTask.model.ResponseContactInfoModel;
import com.bitespeed.backendTask.repository.ContactRepository;
import com.bitespeed.backendTask.service.ResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.*;
@Service
public class ResponseServiceImpl implements ResponseService {

    private final Logger log = LoggerFactory.getLogger(ResponseServiceImpl.class);

    @Autowired
    private ContactRepository contactRepository;

    public ResponseContactInfoModel getContactInfoViaEmailAndPhoneNumber(String email, String phoneNumber) {
        try {

            log.info("Fetching contact information for email: {} and phone number: {}", email, phoneNumber);


            ResponseContactInfoModel response = new ResponseContactInfoModel();
            List<String> emails = new ArrayList<>();
            List<String> phoneNumbers = new ArrayList<>();
            List<Long> secondaryIds = new ArrayList<>();

            // Retrieve contacts based on email or phone number
            List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);
            Contact primaryContact = contacts.stream()
                    .filter(c -> "primary".equals(c.getLinkPrecedence()))
                    .findFirst()
                    .orElse(null);
            Long linkedId = null;
            if (primaryContact == null && !contacts.isEmpty()) {
                // If primary contact is null, take any contact from the list and assign its linked ID as primary contact ID
                for (Contact c : contacts) {
                    if (c.getLinkedId() != null) {
                        response.setPrimaryContactId(c.getLinkedId());
                        linkedId = c.getLinkedId();
                        break;
                    }
                }
            } else if (primaryContact != null) {
                // If primary contact is found, populate its information
                response.setPrimaryContactId(primaryContact.getId());
                emails.add(0, primaryContact.getEmail());
                phoneNumbers.add(0, primaryContact.getPhoneNumber());
            }

            // Populate secondary contact information
            for (Contact c : contacts) {
                if (!"primary".equals(c.getLinkPrecedence())) {
                    emails.add(c.getEmail());
                    phoneNumbers.add(c.getPhoneNumber());
                    secondaryIds.add(c.getId());
                }
            }

            if (linkedId != null) {
                primaryContact = contactRepository.findById(linkedId).orElse(null);
                if (primaryContact != null) {
                    emails.add(0, primaryContact.getEmail());
                    phoneNumbers.add(0, primaryContact.getPhoneNumber());
                } else {
                    log.warn("Primary contact with ID {} not found", linkedId);
                }
            } else {
                log.warn("Linked ID is null, unable to retrieve primary contact details");
            }

            // Set the lists in the response model
            response.setEmails(emails);
            response.setPhoneNumbers(phoneNumbers);
            response.setSecondaryContactIds(secondaryIds);

            // Log debug level message indicating successful completion of contact information fetching
            log.debug("Contact information fetched successfully");
            return response;
        } catch (Exception e) {
            // Log error level message if an exception occurs during the execution of the method
            log.error("An error occurred while fetching contact information: {}", e.getMessage(), e);
            return null;
        }
    }


    public ResponseContactInfoModel getContactViaEmail(String email) {
        try {
            Optional<Contact> primaryContact = contactRepository.findByEmailAndLinkPrecedence(email,"primary");
            ResponseContactInfoModel response = new ResponseContactInfoModel();
            List<String> emails = new ArrayList<>();
            List<String> phoneNumbers = new ArrayList<>();
            List<Long> secondaryIds = new ArrayList<>();


            if(!primaryContact.isEmpty())
            {
                log.info("primary contact is not empty : {}", primaryContact);
                List<Contact> contacts = contactRepository.findByLinkedId(primaryContact.get().getId());
                log.info("findByLinked : {}",contacts);
                emails.add(0,primaryContact.get().getEmail());
                phoneNumbers.add(0,primaryContact.get().getPhoneNumber());
                for (Contact c: contacts){
                    emails.add(c.getEmail());
                    phoneNumbers.add(c.getPhoneNumber());
                    secondaryIds.add(c.getId());
                }

                response.setPrimaryContactId(primaryContact.get().getId());
                response.setEmails(emails);
                response.setPhoneNumbers(phoneNumbers);
                response.setSecondaryContactIds(secondaryIds);
                return response;
            }else{
                log.info("primary contact is empty : {}");
                Set<Contact> contactSet = new HashSet<>();

                List<Contact> contacts = contactRepository.findByEmail(email);
                log.info("findByEmail: {}",contacts);
                if(!contacts.isEmpty()) {
                    Long linkedId = contacts.get(0).getLinkedId();

                    Contact primary = contactRepository.findById(linkedId).get();
                    contactSet.add(primary);
                    List<Contact> secondaryConnection = contactRepository.findByLinkedId(primary.getId());
                    for (Contact c : secondaryConnection) {
                        contactSet.add(c);
                    }

                    emails.add(0, primary.getEmail());
                    phoneNumbers.add(0, primary.getPhoneNumber());
                    for (Contact c : contactSet) {
                        if (c.getLinkedId() != null) {
                            emails.add(c.getEmail());
                            phoneNumbers.add(c.getPhoneNumber());
                            secondaryIds.add(c.getId());
                        }
                    }
                    response.setPrimaryContactId(primary.getId());
                    response.setEmails(emails);
                    response.setPhoneNumbers(phoneNumbers);
                    response.setSecondaryContactIds(secondaryIds);


                    return response;
                }
                return null;
            }

        } catch (Exception e) {
            // Log error level message if an exception occurs during the execution of the method
            log.error("An error occurred while fetching contact information: {}", e.getMessage(), e);
            return null;
        }
    
    }




    public ResponseContactInfoModel getContactViaPhoneNumber(String phoneNumber){
        try{
            Optional<Contact> primaryContact = contactRepository.findByPhoneNumberAndLinkPrecedence(phoneNumber,"primary");
            ResponseContactInfoModel response = new ResponseContactInfoModel();
            List<String> emails = new ArrayList<>();
            List<String> phoneNumbers = new ArrayList<>();
            List<Long> secondaryIds = new ArrayList<>();


            if(!primaryContact.isEmpty())
            {
                log.info("primary contact is not empty : {}", primaryContact);
                List<Contact> contacts = contactRepository.findByLinkedId(primaryContact.get().getId());
                log.info("findByLinked : {}",contacts);
                emails.add(0,primaryContact.get().getEmail());
                phoneNumbers.add(0,primaryContact.get().getPhoneNumber());
                for (Contact c: contacts){
                    emails.add(c.getEmail());
                    phoneNumbers.add(c.getPhoneNumber());
                    secondaryIds.add(c.getId());
                }

                response.setPrimaryContactId(primaryContact.get().getId());
                response.setEmails(emails);
                response.setPhoneNumbers(phoneNumbers);
                response.setSecondaryContactIds(secondaryIds);
                return response;
            }else {
                log.info("primary contact is empty : {}");
                Set<Contact> contactSet = new HashSet<>();


                List<Contact> contacts = contactRepository.findByPhoneNumber(phoneNumber);
                log.info("findByphoneNumber: {}", contacts);
                if(!contacts.isEmpty()){
                Long linkedId = contacts.get(0).getLinkedId();

                Contact primary = contactRepository.findById(linkedId).get();
                contactSet.add(primary);
                List<Contact> secondaryConnection = contactRepository.findByLinkedId(primary.getId());
                for (Contact c : secondaryConnection) {
                    contactSet.add(c);
                }

                emails.add(0, primary.getEmail());
                phoneNumbers.add(0, primary.getPhoneNumber());
                for (Contact c : contactSet) {
                    if (c.getLinkedId() != null) {
                        emails.add(c.getEmail());
                        phoneNumbers.add(c.getPhoneNumber());
                        secondaryIds.add(c.getId());
                    }
                }
                response.setPrimaryContactId(primary.getId());
                response.setEmails(emails);
                response.setPhoneNumbers(phoneNumbers);
                response.setSecondaryContactIds(secondaryIds);


                return response;
            }
            return null;

            }

        } catch (Exception e) {
            // Log error level message if an exception occurs during the execution of the method
            log.error("An error occurred while fetching contact information: {}", e.getMessage(), e);
            return null;
        }
    }
}
