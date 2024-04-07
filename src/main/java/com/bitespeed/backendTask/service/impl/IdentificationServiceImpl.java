package com.bitespeed.backendTask.service.impl;

import com.bitespeed.backendTask.entity.Contact;
import com.bitespeed.backendTask.model.RequestContactInfoModel;
import com.bitespeed.backendTask.model.ResponseContactInfoModel;
import com.bitespeed.backendTask.repository.ContactRepository;
import com.bitespeed.backendTask.service.IdentificationService;
import com.bitespeed.backendTask.service.IdentityOperationService;
import com.bitespeed.backendTask.service.ResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IdentificationServiceImpl implements IdentificationService {

    private final Logger log = LoggerFactory.getLogger(IdentificationServiceImpl.class);

    @Autowired
    private IdentityOperationService identityOperationServiceImpl;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ResponseService responseServiceImpl;

    public ResponseContactInfoModel fetchContactInfo(RequestContactInfoModel contactModel) {
            String email = contactModel.getEmail();
            String phoneNumber = contactModel.getPhoneNumber();
        try {
            log.info("Fgitetching contact information for email: {} and phone number: {}", email, phoneNumber);
            List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

            if (contacts.isEmpty()) {
                log.info("No contacts found for email: {} or phone number: {}", email, phoneNumber);
                identityOperationServiceImpl.createNewUser(email, phoneNumber);
                return responseServiceImpl.getContactInfoViaEmailAndPhoneNumber(email, phoneNumber);
            } else {
                Contact contact = contactRepository.findByEmailAndPhoneNumber(email, phoneNumber);

                if (contact == null) {
                    log.info("Contact found for email: {} and phone number: {}", email, phoneNumber);

                    List<Contact> fetchedViaEmail = contacts.stream()
                            .filter(c -> contactModel.getEmail().equals(c.getEmail()))
                            .collect(Collectors.toList());
                    log.info("Fetched contacts via email: {}", fetchedViaEmail);

                    List<Contact> fetchedViaPhoneNumber = contacts.stream()
                            .filter(c -> contactModel.getPhoneNumber().equals(c.getPhoneNumber()))
                            .collect(Collectors.toList());
                    log.info("Fetched contacts via phone number: {}", fetchedViaPhoneNumber);

                    if (fetchedViaEmail.isEmpty()) {
                        userExistViaPhoneNumber(email, fetchedViaPhoneNumber);
                        return responseServiceImpl.getContactInfoViaEmailAndPhoneNumber(email, phoneNumber);
                    } else if (fetchedViaPhoneNumber.isEmpty()){
                        userExistViaEmail(phoneNumber, fetchedViaEmail);
                        return responseServiceImpl.getContactInfoViaEmailAndPhoneNumber(email, phoneNumber);
                    }

                    else {
                        Optional<Contact> optionalP1 = fetchedViaEmail.stream()
                                .filter(c -> "primary".equals(c.getLinkPrecedence()))
                                .findFirst();

                        Optional<Contact> optionalP2 = fetchedViaPhoneNumber.stream()
                                .filter(c -> "primary".equals(c.getLinkPrecedence()))
                                .findFirst();

                        if (optionalP1.isPresent() && optionalP2.isPresent()) {
                            Contact p1 = optionalP1.get();
                            Contact p2 = optionalP2.get();

                            Date p1CreatedAt = p1.getCreatedAt();
                            Date p2CreatedAt = p2.getCreatedAt();

                            // Compare createdAt dates
                            if (p1CreatedAt.before(p2CreatedAt)) {

                                p2.setLinkedId(p1.getId());
                                p2.setLinkPrecedence("secondary");
                                contactRepository.save(p2);
                            } else {

                                p1.setLinkedId(p2.getId());
                                p1.setLinkPrecedence("secondary");
                                contactRepository.save(p1);
                            }
                        }

                        return responseServiceImpl.getContactInfoViaEmailAndPhoneNumber(email, phoneNumber);
                    }
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while fetching contact information: {}", e.getMessage(), e);
        }
        return responseServiceImpl.getContactInfoViaEmailAndPhoneNumber(email, phoneNumber);
    }

    private void userExistViaPhoneNumber(String email, List<Contact> fetchedViaPhoneNumber) {
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
            } else {
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

    private void userExistViaEmail(String phoneNumber, List<Contact> fetchedViaEmail) {
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
            } else {
                secondaryContact.setEmail(primaryContact.getEmail());
                secondaryContact.setPhoneNumber(phoneNumber);
                secondaryContact.setLinkPrecedence("secondary");
                secondaryContact.setLinkedId(primaryContact.getId());
                contactRepository.save(secondaryContact);
            }


        } catch (Exception e) {
            log.error("An error occurred while processing user existence via email: {}", e.getMessage(), e);
        }
    }

    public ResponseContactInfoModel fetchContactInfoViaEmail(String email) {
        try{
            return responseServiceImpl.getContactViaEmail(email);
        }catch (Exception e){
            log.error("An error occurred while fetching contact information: {}", e.getMessage(), e);
            return null;
        }
    }

    public ResponseContactInfoModel fetchContactInfoViaPhoneNumber(String phoneNumber) {
        try{
            return responseServiceImpl.getContactViaPhoneNumber(phoneNumber);
        }catch (Exception e){
            log.error("An error occurred while fetching contact information: {}", e.getMessage(), e);
            return null;
        }
    }
}
