package com.bitespeed.backendTask.service.impl;

import com.bitespeed.backendTask.entity.Contact;
import com.bitespeed.backendTask.repository.ContactRepository;
import com.bitespeed.backendTask.service.IdentityOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityOperationServiceImpl implements IdentityOperationService {
    Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ContactRepository contactRepository;

    public void createNewUser(String email , String phoneNumber){
        log.info("Inside createNewUserMethods: {} : {}", email, phoneNumber);
        try{
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence("primary");

            contactRepository.save(newContact);
            log.info("Added as new User");
//            return contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

        }catch(Exception e){
            log.error("An error occurred while creating a new user: {}", e.getMessage(), e);
//            return null;
        }
    }


}
