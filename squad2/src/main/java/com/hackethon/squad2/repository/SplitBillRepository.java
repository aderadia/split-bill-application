package com.hackethon.squad2.repository;

import com.hackethon.squad2.model.SplitBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SplitBillRepository extends JpaRepository<SplitBill, UUID> {
    Optional<SplitBill> findByBillId(String billId);

    List<SplitBill> findBySofId(UUID sof);
}
