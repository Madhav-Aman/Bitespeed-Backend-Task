package com.bitespeed.backendTask.service;

import com.bitespeed.backendTask.model.ResponseContactInfoModel;
import org.springframework.stereotype.Service;

@Service
public interface ResponseService {

    ResponseContactInfoModel getContactInfoViaEmailAndPhoneNumber(String email, String phoneNumber);
    ResponseContactInfoModel getContactViaEmail(String email);
    ResponseContactInfoModel getContactViaPhoneNumber(String phoneNumber);

}
