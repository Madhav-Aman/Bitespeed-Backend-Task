package com.bitespeed.backendTask.repository;

import com.bitespeed.backendTask.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {


    Contact findByEmailAndPhoneNumber(String email, String phoneNumber);
    @Query("SELECT c FROM Contact c WHERE c.email = :email OR c.phoneNumber = :phoneNumber")
    List<Contact> findByEmailOrPhoneNumber(@Param("email") String email, @Param("phoneNumber") String phoneNumber);

    List<Contact> findByEmail(String email);

    List<Contact> findByPhoneNumber(String phoneNumber);


    Optional<Contact> findByEmailAndLinkPrecedence(String email, String precedence);
    Contact findByPhoneNumberAndLinkPrecedence(String phoneNumber,String precedence);

    List<Contact> findByLinkedId(Long id);


}
