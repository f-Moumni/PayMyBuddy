package com.pmb.paymybuddy.repository;

import com.pmb.paymybuddy.model.BankAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount,Long> {

    @Query(value = "SELECT bank_account.idbank_account, bank_account.iban ,bank_account.swift,bank_account.owner FROM paymybuddy.bank_account join user on (bank_account.owner =user.iduser) join pmb_account on( user.iduser =pmb_account.owner) where pmb_account.email like :email", nativeQuery = true)
    Optional<BankAccount> findByOwner_Account_Mail(@Param("email")String mail);
}
