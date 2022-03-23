package com.pmb.paymybuddy.repository;

import com.pmb.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

public Optional<User> findByAccount_Mail(String mail);
}
