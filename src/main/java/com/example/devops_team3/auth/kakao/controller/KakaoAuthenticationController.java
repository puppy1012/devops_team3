package com.example.devops_team3.auth.kakao.controller;

import com.example.devops_team3.auth.kakao.service.KakaoAuthenticationService;
import com.example.devops_team3.auth.kakao.service.response.KakaoLoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account/kakao-authentication")
public class KakaoAuthenticationController {

    final private KakaoAuthenticationService kakaoAuthenticationService;

    @GetMapping("/login")
    @Transactional
    public void requestAccessToken(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.info("requestAccessToken(): code {}", code);
        try {
            KakaoLoginResponse loginResponse = kakaoAuthenticationService.handleLogin(code);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(loginResponse.getHtmlResponse());
        } catch (Exception e){
            log.error("Kakao 로그인 에러", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "카카오 로그인 실패: "+e.getMessage());
        }
    }
}
