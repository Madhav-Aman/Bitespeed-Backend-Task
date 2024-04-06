package com.bitespeed.backendTask.service.impl;

import com.bitespeed.backendTask.entity.Contact;
import com.bitespeed.backendTask.model.ResponseContactInfoModel;
import com.bitespeed.backendTask.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseServices {
    @Autowired
    private ContactRepository contactRepository;

    public ResponseContactInfoModel getContactInfo(String email, String phoneNumber) {
        try {
            List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);
            ResponseContactInfoModel response = new ResponseContactInfoModel();
            Contact primaryContact = null;
            List<Long> secondaryContactsID = new ArrayList<>();
            List<String> phoneNumbers = new ArrayList<>();
            List<String> emails = new ArrayList<>();

            for (Contact c : contacts) {
                if ("primary".equals(c.getLinkPrecedence())) {
                    primaryContact = c;
                } else {
                    secondaryContactsID.add(c.getId());
                }
                phoneNumbers.add(c.getPhoneNumber());
                emails.add(c.getEmail());
            }

            if (primaryContact != null) {
                phoneNumbers.add(0, primaryContact.getPhoneNumber());
                emails.add(0, primaryContact.getEmail());
                response.setPrimaryContactId(primaryContact.getId());
            }

            response.setEmails(emails);
            response.setPhoneNumbers(phoneNumbers);
            response.setSecondaryContactIds(secondaryContactsID);

            return response;

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            return null;
        }
    }
}
