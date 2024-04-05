package com.bitespeed.backendTask.controller;

import com.bitespeed.backendTask.model.RequestContactInfoModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping("/identify")
    public ResponseEntity<?> identifyUser(@RequestBody RequestContactInfoModel contactModel){
        return null;
    }






}
