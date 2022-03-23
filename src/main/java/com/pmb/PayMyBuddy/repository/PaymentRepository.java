package com.pmb.PayMyBuddy.repository;

import com.pmb.PayMyBuddy.model.Payment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment,Long> {


    Iterable<Payment> findByDebitAccount(String mail);
    Iterable<Payment> findByCreditAccount(String mail);

}
