package com.example.devops_team3.account.controller.response;

import com.example.devops_team3.account.entity.Account;

public class MyProfileAccountResponse {
    private String email;
    private String nickName;

    public MyProfileAccountResponse() {
    }

    public MyProfileAccountResponse(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }

    public static MyProfileAccountResponse from(Account account) {
        return new MyProfileAccountResponse(
                account.getEmail(),
                account.getNickName());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}

