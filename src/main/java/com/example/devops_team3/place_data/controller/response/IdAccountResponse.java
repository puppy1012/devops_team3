package com.example.devops_team3.place_data.controller.response;

public class IdAccountResponse {
    private Long accountId;

    public IdAccountResponse() {
    }

    public IdAccountResponse(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
