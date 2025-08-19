package com.example.devops_team3.account.controller.response;

public class UpdateAccountResponse {
    private String message;

    public UpdateAccountResponse() {
    }

    public UpdateAccountResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
