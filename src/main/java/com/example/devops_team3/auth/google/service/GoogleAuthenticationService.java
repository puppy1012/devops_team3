package com.example.devops_team3.auth.google.service;

import com.example.devops_team3.auth.google.service.response.GoogleLoginResponse;

public interface GoogleAuthenticationService {
    GoogleLoginResponse handleLogin(String code);
}
