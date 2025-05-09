package com.hackethon.squad2.repository;

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
public interface MutationRepository extends JpaRepository<Mutations, UUID> {

    @Query("SELECT t FROM Mutations t WHERE t.sof.accNum = :accNum")
    Page<Mutations> findAllBySofPage(@Param("accNum") String accountNumber, Pageable pageable);
    @Query("SELECT t FROM Mutations t WHERE t.sof.accNum = :accNum")
    List<Mutations> findAllBySof(@Param("accNum") String accountNumber);

}
