package com.bitespeed.backendTask.service.impl;

import com.bitespeed.backendTask.entity.Contact;
import com.bitespeed.backendTask.model.RequestContactInfoModel;
import com.bitespeed.backendTask.model.ResponseContactInfoModel;
import com.bitespeed.backendTask.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseService {

    private final Logger log = LoggerFactory.getLogger(ResponseService.class);

    @Autowired
    private ContactRepository contactRepository;

    public ResponseContactInfoModel getContactInfoViaEmailAndPhoneNumber(String email, String phoneNumber) {
        try {
            // Log info level message for the start of the method execution
            log.info("Fetching contact information for email: {} and phone number: {}", email, phoneNumber);

            // Initialize the response model and lists for email, phone numbers, and secondary IDs
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


    public ResponseContactInfoModel getContactViaEmail(String email){
        try {
            ResponseContactInfoModel response = new ResponseContactInfoModel();
            List<Contact> contacts = contactRepository.findByEmail(email);
            if (contacts.isEmpty()) {

                Contact contact = new Contact();
                contact.setEmail(email);
                contact.setLinkPrecedence("primary");
                contactRepository.save(contact);


                return getContactInfoViaEmailAndPhoneNumber(email,"");

            } else {


                List<String> emails = new ArrayList<>();
                List<String> phoneNumbers = new ArrayList<>();
                List<Long> secondaryIds = new ArrayList<>();

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
            }} catch(Exception e){
                // Log error level message if an exception occurs during the execution of the method
                log.error("An error occurred while fetching contact information: {}", e.getMessage(), e);
                return null;
            }

    }


    public ResponseContactInfoModel getContactViaPhoneNumber(String phoneNumber){
        try{
            ResponseContactInfoModel response = new ResponseContactInfoModel();
            List<Contact> contacts = contactRepository.findByPhoneNumber(phoneNumber);
            if (contacts.isEmpty()) {

                Contact contact = new Contact();
                contact.setPhoneNumber(phoneNumber);
                contact.setLinkPrecedence("primary");
                contactRepository.save(contact);


                return getContactInfoViaEmailAndPhoneNumber("",phoneNumber);

            } else {
                List<String> emails = new ArrayList<>();
                List<String> phoneNumbers = new ArrayList<>();
                List<Long> secondaryIds = new ArrayList<>();

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
            }
        } catch (Exception e) {
            // Log error level message if an exception occurs during the execution of the method
            log.error("An error occurred while fetching contact information: {}", e.getMessage(), e);
            return null;
        }
    }
}
