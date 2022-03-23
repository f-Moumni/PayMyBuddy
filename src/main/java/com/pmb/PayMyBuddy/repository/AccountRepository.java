package com.pmb.paymybuddy.repository;

import com.pmb.paymybuddy.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account,Long> {

     Optional<Account> findByMail (String mail);

}
