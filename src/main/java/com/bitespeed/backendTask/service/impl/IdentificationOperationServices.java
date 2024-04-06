package com.bitespeed.backendTask.service.impl;

import com.bitespeed.backendTask.entity.Contact;
import com.bitespeed.backendTask.model.ResponseContactInfoModel;
import com.bitespeed.backendTask.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class IdentificationOperationServices {
    Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ResponseServices responseServices;


    public ResponseContactInfoModel createNewUser(String email , String phoneNumber){
        log.info(" inside createNewUserMethods : {} : {}",email,phoneNumber);
        try{
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence("primary");

            contactRepository.save(newContact);
            log.info("added as new User");
            return responseServices.getContactInfo(email, phoneNumber);

        }catch(Exception e){
            return null;
        }

    }
}
