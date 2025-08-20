package com.example.devops_team3.auth.kakao.service.response;

public class ExistingUserKakaoLoginResponse extends KakaoLoginResponse {
    private final String htmlResponse;

    public ExistingUserKakaoLoginResponse(String token, String nickname, String email, String targetOrigin) {
        String safeOrigin = targetOrigin;
        if (!(safeOrigin.startsWith("http://") || safeOrigin.startsWith("https://"))) {
            // 환경이 http면 http:// 로, 배포는 https:// 로 맞추세요
            safeOrigin = "http://" + safeOrigin;
        }
        this.htmlResponse = """
        <html><body><script>
        window.opener.postMessage({
            userToken: '%s',
            user: { name: '%s', email: '%s' }
        }, '%s'); window.close();
        </script></body></html>
        """.formatted(token, escape(nickname), escape(email), safeOrigin);
    }


    @Override
    public String getHtmlResponse() {
        return htmlResponse;
    }
}
