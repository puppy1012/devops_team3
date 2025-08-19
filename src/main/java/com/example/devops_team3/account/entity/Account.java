package com.example.devops_team3.account.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(
        name = "account",
        uniqueConstraints = {
            @UniqueConstraint(
                name = "uk_email_login_type",
                columnNames = {"email","login_type_id"}
                )
            }
        )
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String email;
    String password;
    String nickName;

    @ManyToOne
    @JoinColumn(name = "login_type_id", nullable = false)
    private AccountLoginType loginType;

    public Account() {
    }

    public Account (String email, String password, String nickName, AccountLoginType loginType) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.loginType = loginType;
    }

    public void setLoginType(AccountLoginType loginType) {
        this.loginType = loginType;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
