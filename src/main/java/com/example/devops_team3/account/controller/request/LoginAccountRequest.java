package com.example.devops_team3.account.controller.request;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAccountRequest {
    private String email;
    private String password;
}
