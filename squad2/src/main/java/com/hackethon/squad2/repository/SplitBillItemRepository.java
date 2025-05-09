package com.hackethon.squad2.repository;

import com.hackethon.squad2.model.SplitBillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SplitBillItemRepository extends JpaRepository<SplitBillItem, UUID> {
}
