package com.pmb.PayMyBuddy.repository;

import com.pmb.PayMyBuddy.model.Transfer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends CrudRepository<Transfer,Long> {

    public Iterable<Transfer> findByDebitAccount (String mail);
}
