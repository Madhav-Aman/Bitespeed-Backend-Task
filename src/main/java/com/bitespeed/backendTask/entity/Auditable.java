package com.bitespeed.backendTask.entity;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface Auditable {

    void setCreatedAt(Date createdAt);
    void setUpdatedAt(Date updatedAt);

    void setDeletedAt(Date deletedAt);
}
