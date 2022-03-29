package com.pmb.PayMyBuddy.repository;

import com.pmb.PayMyBuddy.model.AppUser;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AppUser,Long> {

public Optional<AppUser> findByAccount_Mail(String mail);
}
