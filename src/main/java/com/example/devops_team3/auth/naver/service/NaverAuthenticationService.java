package com.example.devops_team3.auth.naver.service;

import com.example.devops_team3.auth.naver.service.response.NaverLoginResponse;

public interface NaverAuthenticationService {
    NaverLoginResponse handleLogin(String code, String state);
}
