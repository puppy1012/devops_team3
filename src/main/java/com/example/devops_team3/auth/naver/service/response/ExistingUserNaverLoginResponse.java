package com.example.devops_team3.auth.naver.service.response;

public class ExistingUserNaverLoginResponse extends NaverLoginResponse {
    private final String htmlResponse;

    public ExistingUserNaverLoginResponse(String token, String nickname, String email, String origin) {
        this.htmlResponse = """
        <html><body><script>
        window.opener.postMessage({
            userToken: '%s',
            user: { name: '%s', email: '%s' }
        }, '%s'); window.close();
        </script></body></html>
        """.formatted(token, escape(nickname), escape(email), origin);
    }

    @Override
    public String getHtmlResponse() {
        return htmlResponse;
    }
}
