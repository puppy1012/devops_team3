package com.example.devops_team3.auth.google.controller;

import com.example.devops_team3.auth.google.service.GoogleAuthenticationService;
import com.example.devops_team3.auth.google.service.response.GoogleLoginResponse;
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
@RequestMapping("/account/google-authentication")
public class GoogleAuthenticationController {

    final private GoogleAuthenticationService googleAuthenticationService;

    @GetMapping("/login")
    @Transactional
    public void requestAccessToken(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.info("requestAccessToken(): code {}", code);
        try {
            GoogleLoginResponse loginResponse = googleAuthenticationService.handleLogin(code);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(loginResponse.getHtmlResponse());
        } catch (Exception e){
            log.error("Google 로그인 에러", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "구글 로그인 실패: "+e.getMessage());
        }
    }
}
