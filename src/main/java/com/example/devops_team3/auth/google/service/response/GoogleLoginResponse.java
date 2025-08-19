package com.example.devops_team3.auth.google.service.response;

public abstract class GoogleLoginResponse {
    public abstract String getHtmlResponse();

    protected static String escape(String str) {
        return str.replace("'", "\\'");
    }
}
