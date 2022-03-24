package com.pmb.PayMyBuddy.repository;

import com.pmb.PayMyBuddy.model.Account;
import com.pmb.PayMyBuddy.model.Transfer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends CrudRepository<Transfer,Long> {
    @Query(value = "SELECT * FROM paymybuddy.transaction join pmb_account on (pmb_account.account_id =transaction.debit_account)where transaction.transaction_type like \"TRANSFER\" and pmb_account.email like :email"
            , nativeQuery = true)
    public Iterable<Transfer> findByDebitAccount (@Param("email")String email);
    @Query(value = "SELECT * FROM paymybuddy.transaction join pmb_account on (pmb_account.account_id =transaction.credit_account)where transaction.transaction_type like \"TRANSFER\" and pmb_account.email like :email"
            , nativeQuery = true)
    public Iterable<Transfer> findByCreditAccount (@Param("email")String email);
}
