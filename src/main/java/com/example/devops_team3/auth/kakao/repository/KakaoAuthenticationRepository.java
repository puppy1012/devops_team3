package com.example.devops_team3.auth.kakao.repository;

import java.util.Map;

public interface KakaoAuthenticationRepository {
    String getLoginLink();
    Map<String, Object> getAccessToken(String code);
    Map<String, Object> getUserInfo(String accessToken);
}
