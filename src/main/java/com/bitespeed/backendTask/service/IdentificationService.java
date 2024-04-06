package com.bitespeed.backendTask.service;

import com.bitespeed.backendTask.model.RequestContactInfoModel;
import com.bitespeed.backendTask.model.ResponseContactInfoModel;
import org.springframework.stereotype.Service;

@Service
public interface IdentificationService {
    ResponseContactInfoModel fetchContactInfo(RequestContactInfoModel contactModel);

    ResponseContactInfoModel fetchContactInfoViaEmail(String email);

    ResponseContactInfoModel fetchContactInfoViaPhoneNumber(String phoneNumber);
}
