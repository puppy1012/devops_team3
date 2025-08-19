package com.example.devops_team3.account.repository;

import com.example.devops_team3.account.entity.Account;
import com.example.devops_team3.account.entity.AccountLoginType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByEmailAndLoginType(String requestEmail, AccountLoginType type);
}
