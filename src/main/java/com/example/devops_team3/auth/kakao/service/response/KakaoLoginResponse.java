package com.example.devops_team3.auth.kakao.service.response;

public abstract class KakaoLoginResponse {
    public abstract String getHtmlResponse();

    protected static String escape(String str) {
        return str.replace("'", "\\'");
    }
}
