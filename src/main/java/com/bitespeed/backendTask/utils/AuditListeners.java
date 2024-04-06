package com.bitespeed.backendTask.utils;

import com.bitespeed.backendTask.entity.Contact;
import com.sun.tools.jconsole.JConsoleContext;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.stereotype.Component;

import java.util.Date;


public class AuditListeners {


    @PrePersist
    public void setCreateFields(Object target){
        if(target instanceof Contact){
            Contact contact = (Contact) target;

            contact.setCreatedAt(new Date());
        }
    }

    @PreUpdate
    public void setUpdateFields(Object target){
        if(target instanceof Contact){
            Contact contact = (Contact) target;
            contact.setUpdatedAt(new Date());
        }
    }


    @PreRemove
    public void setDeleteFields(Object target){
        if(target instanceof Contact){
            Contact contact = (Contact) target;
            contact.setDeletedAt(new Date());
        }
    }
}
