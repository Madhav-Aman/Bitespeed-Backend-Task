package com.bitespeed.backendTask.controller;

import com.bitespeed.backendTask.model.RequestContactInfoModel;
import com.bitespeed.backendTask.service.impl.IdentificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IdentificationService identificationService;

    @PostMapping("/identify")
    public ResponseEntity<?> identifyUser(@RequestBody RequestContactInfoModel contactModel) {
        String email = contactModel.getEmail();
        String phoneNumber = contactModel.getPhoneNumber();
        try {
            if (email != null && phoneNumber != null) {
                log.info("Inside where both email and phone number exist");
                return ResponseEntity.ok(identificationService.fetchContactInfo(contactModel));
            } else if (email == null && phoneNumber != null) {
                log.info("Inside where email does not exist and phoneNumber exists");
                return ResponseEntity.ok(identificationService.fetchContactInfoViaPhoneNumber(phoneNumber));
            } else if (email != null && phoneNumber == null) {
                log.info("Inside where email exists and phoneNumber does not exist");
                return ResponseEntity.ok(identificationService.fetchContactInfoViaEmail(email));
            }
        } catch (Exception e) {
            log.error("An error occurred while identifying user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
        // If no conditions are met, return bad request
        return ResponseEntity.badRequest().body("Invalid request parameters.");
    }
}
