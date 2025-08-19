package com.example.devops_team3.auth.naver.service.response;

public abstract class NaverLoginResponse {
    public abstract String getHtmlResponse();

    protected static String escape(String str) {
        return str.replace("'", "\\'");
    }
}
