package com.bitespeed.backendTask.service;

import org.springframework.stereotype.Service;

@Service
public interface IdentityOperationService {

    void createNewUser(String email , String phoneNumber);
}
