package com.pmb.PayMyBuddy.repository;

import com.pmb.PayMyBuddy.model.Account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {

     Optional<Account> findByMail (String mail);
     Boolean existsByMail(String mail);

}
