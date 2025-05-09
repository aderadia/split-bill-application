package com.hackethon.squad2.repository;

import com.hackethon.squad2.model.Sof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SofRepository extends JpaRepository<Sof, UUID> {
    Sof findByAccNum(String accNum);
}
