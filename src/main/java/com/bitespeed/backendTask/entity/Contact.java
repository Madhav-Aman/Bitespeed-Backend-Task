package com.bitespeed.backendTask.entity;

import com.bitespeed.backendTask.utils.AuditListeners;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Contact")
@EntityListeners(AuditListeners.class)
public class Contact implements Auditable{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "phoneNumber")
        private String phoneNumber;

        @Column(name = "email")
        private String email;

        @Column(name = "linkedId")
        private Long linkedId;

        @Column(name = "linkPrecedence")
        private String linkPrecedence;

        @Column(name = "createdAt")
        @Temporal(TemporalType.TIMESTAMP)
        private Date createdAt;

        @Column(name = "updatedAt")
        @Temporal(TemporalType.TIMESTAMP)
        private Date updatedAt;

        @Column(name = "deletedAt")
        @Temporal(TemporalType.TIMESTAMP)
        private Date deletedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(Long linkedId) {
        this.linkedId = linkedId;
    }

    public String getLinkPrecedence() {
        return linkPrecedence;
    }

    public void setLinkPrecedence(String linkPrecedence) {
        this.linkPrecedence = linkPrecedence;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Contact(Long id, String phoneNumber, String email, Long linkedId, String linkPrecedence, Date createdAt, Date updatedAt, Date deletedAt) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.linkedId = linkedId;
        this.linkPrecedence = linkPrecedence;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Contact() {
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", linkedId=" + linkedId +
                ", linkPrecedence='" + linkPrecedence + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
