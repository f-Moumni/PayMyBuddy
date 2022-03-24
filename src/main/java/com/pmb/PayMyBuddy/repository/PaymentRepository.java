package com.pmb.PayMyBuddy.repository;

import com.pmb.PayMyBuddy.model.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.LongSummaryStatistics;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    @Query(value = "SELECT * FROM paymybuddy.transaction join pmb_account on (pmb_account.account_id =transaction.debit_account)where transaction.transaction_type like \"PAYMENT\" and pmb_account.email like :email"
            , nativeQuery = true)
    Iterable<Payment> findByDebitAccount(@Param("email")String email);
    @Query(value = "SELECT * FROM paymybuddy.transaction join pmb_account on (pmb_account.account_id =transaction.credit_account)where transaction.transaction_type like \"PAYMENT\" and pmb_account.email like :email"
            , nativeQuery = true)
    Iterable<Payment> findByCreditAccount(@Param("email")String email);
}
