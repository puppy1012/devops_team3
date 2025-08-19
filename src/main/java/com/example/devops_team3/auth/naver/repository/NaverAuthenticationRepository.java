package com.example.devops_team3.auth.naver.repository;

import java.util.Map;

public interface NaverAuthenticationRepository {
    Map<String, Object> getAccessToken(String code, String state);
    Map<String, Object> getUserInfo(String accessToken);
}
