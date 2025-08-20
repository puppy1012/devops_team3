package com.example.devops_team3.auth.kakao.service;

import com.example.devops_team3.auth.kakao.service.response.KakaoLoginResponse;

public interface KakaoAuthenticationService {
    KakaoLoginResponse handleLogin(String code);
    String getLoginLink();
}
