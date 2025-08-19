package com.example.devops_team3.auth.google.repository;

import java.util.Map;

public interface GoogleAuthenticationRepository {
    Map<String, Object> getAccessToken(String code);
    Map<String, Object> getUserInfo(String accessToken);
}
