package com.bitespeed.backendTask.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
public class ResponseContactInfoModel implements Serializable {

    private Long primaryContactId;
    private List<String> emails;
    private List<String> phoneNumbers;
    private List<Long> secondaryContactIds;

    public ResponseContactInfoModel(Long primaryContactId, List<String> emails, List<String> phoneNumbers, List<Long> secondaryContactIds) {
        this.primaryContactId = primaryContactId;
        this.emails = emails;
        this.phoneNumbers = phoneNumbers;
        this.secondaryContactIds = secondaryContactIds;
    }

    public ResponseContactInfoModel() {
    }

    public Long getPrimaryContactId() {
        return primaryContactId;
    }

    public void setPrimaryContactId(Long primaryContactId) {
        this.primaryContactId = primaryContactId;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<Long> getSecondaryContactIds() {
        return secondaryContactIds;
    }

    public void setSecondaryContactIds(List<Long> secondaryContactIds) {
        this.secondaryContactIds = secondaryContactIds;
    }

    @Override
    public String toString() {
        return "ResponseContactInfoModel{" +
                "primaryContactId=" + primaryContactId +
                ", emails=" + emails +
                ", phoneNumbers=" + phoneNumbers +
                ", secondaryContactIds=" + secondaryContactIds +
                '}';
    }
}
