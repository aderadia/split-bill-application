package com.hackethon.squad2.repository;

import com.hackethon.squad2.model.SplitBillUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SplitBillUserRepository extends JpaRepository<SplitBillUser, UUID> {
    List<SplitBillUser> findBySplitBillId(UUID uuid);
    SplitBillUser findBySofIdAndBillUserId(UUID sofId, String billUserId);
}
