package com.hackethon.squad2.repository;

import com.hackethon.squad2.model.Contact;
import com.hackethon.squad2.model.Mutations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
    @Query("SELECT t FROM Contact t WHERE t.sof.user.id = :userId")
    List<Contact> findAllBySofId(@Param("userId") UUID userId);
}
