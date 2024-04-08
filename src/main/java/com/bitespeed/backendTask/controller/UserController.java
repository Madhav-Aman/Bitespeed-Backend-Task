package com.bitespeed.backendTask.controller;

import com.bitespeed.backendTask.model.RequestContactInfoModel;
import com.bitespeed.backendTask.model.ResponseContactInfoModel;
import com.bitespeed.backendTask.service.IdentificationService;
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
    public ResponseEntity<?> contactInfo(@RequestBody RequestContactInfoModel contactModel) {
        String email = contactModel.getEmail();
        String phoneNumber = contactModel.getPhoneNumber();

            try {
                if (email != null && phoneNumber != null) {
                    log.info("Inside where both email and phone number exist");
                    return ResponseEntity.ok(identificationService.fetchContactInfo(contactModel));
                }

                else if (email == null && phoneNumber != null) {
                    log.info("Inside where email does not exist and phoneNumber exists");
                    ResponseContactInfoModel contactInfo = identificationService.getContactViaPhoneNumber(phoneNumber);
                    if (contactInfo != null) {
                        return ResponseEntity.ok(contactInfo);
                    } else {
                        // Return response indicating that the user doesn't exist via phone number
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist via phone number");
                    }
                }


                else if (email != null && phoneNumber == null) {
                    log.info("Inside where email exists and phoneNumber does not exist");
                    ResponseContactInfoModel contactInfo = identificationService.fetchContactInfoViaEmail(email);
                    if (contactInfo != null) {
                        return ResponseEntity.ok(contactInfo);
                    } else {
                        // Return response indicating that the user doesn't exist via email
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist via email");
                    }
                }
            } catch (Exception e) {
                log.error("An error occurred while identifying user: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
            }
        // If no conditions are met, return bad request
        return ResponseEntity.badRequest().body("Invalid request parameters.");
    }

    @PostMapping("/identify1")
    public ResponseEntity<?> identifyUser(@RequestBody RequestContactInfoModel contactModel) {
        String email = contactModel.getEmail();
        String phoneNumber = contactModel.getPhoneNumber();
        boolean isValidEmail = false;
        boolean isValidPhoneNumber = false;

        // Regular expressions for email and mobile phone number validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        String phoneRegex = "^(?!0|1)\\d{10}$";
        // This regex allows 10-digit phone numbers not starting with 0 or 1

        // Check if email is in a valid format
        if (email == null || email.matches(emailRegex)) {
            isValidEmail = true;
        }

        // Check if phone number is in the format of a mobile number
        if (phoneNumber == null || phoneNumber.matches(phoneRegex)) {
            isValidPhoneNumber = true;
        }

        try {
            if (isValidEmail && isValidPhoneNumber) {
                log.info("Inside where both email and phone number exist");
                return ResponseEntity.ok(identificationService.fetchContactInfo(contactModel));
            }

            else if (!isValidEmail && isValidPhoneNumber) {
                log.info("Inside where email does not exist or is invalid and phoneNumber exists");
                return ResponseEntity.badRequest().body("Invalid email format. Please provide a valid email.");
            }

            else if (isValidEmail && !isValidPhoneNumber) {
                log.info("Inside where email exists and phoneNumber does not exist or is invalid");
                return ResponseEntity.badRequest().body("Invalid phone number format. Please provide a valid mobile phone number.");
            }
        } catch (Exception e) {
            log.error("An error occurred while identifying user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
        // If no conditions are met, return bad request
        return ResponseEntity.badRequest().body("Invalid request parameters.");
    }


}
