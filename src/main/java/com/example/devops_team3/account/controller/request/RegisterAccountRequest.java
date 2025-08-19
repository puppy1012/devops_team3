package com.example.devops_team3.account.controller.request;

import com.example.devops_team3.account.entity.Account;
import com.example.devops_team3.account.entity.AccountLoginType;
import com.example.devops_team3.account.utility.EncryptionUtility;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAccountRequest {
    private String email;
    private String password;
    private String nickName;

    public Account toAccount(AccountLoginType loginType) {
        return new Account(email, EncryptionUtility.encode(password), nickName, loginType);
    }
}
