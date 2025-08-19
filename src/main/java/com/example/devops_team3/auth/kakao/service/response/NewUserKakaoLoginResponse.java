package com.example.devops_team3.auth.kakao.service.response;

public class NewUserKakaoLoginResponse extends KakaoLoginResponse {
    private final String htmlResponse;

    public NewUserKakaoLoginResponse(String token, String nickname, String email, String origin) {
        this.htmlResponse = """
        <html><body><script>
        window.opener.postMessage({
            newUser: true,
            loginType: 'KAKAO',
            temporaryUserToken: '%s',
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
