package com.example.devops_team3.account.repository;

import com.example.devops_team3.account.entity.AccountLoginType;
import com.example.devops_team3.account.entity.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountLoginTypeRepository extends JpaRepository<AccountLoginType, Long> {
    Optional<AccountLoginType> findByLoginType(LoginType loginType);
}
