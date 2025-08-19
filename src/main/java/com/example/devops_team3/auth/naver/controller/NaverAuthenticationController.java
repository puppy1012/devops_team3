package com.example.devops_team3.auth.naver.controller;

import com.example.devops_team3.auth.naver.service.NaverAuthenticationService;
import com.example.devops_team3.auth.naver.service.response.NaverLoginResponse;
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
@RequestMapping("/account/naver-authentication")
public class NaverAuthenticationController {

    final private NaverAuthenticationService naverAuthenticationService;

    @GetMapping("/login")
    @Transactional
    public void requestAccessToken(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response) throws IOException {
        log.info("requestAccessToken(): code {}, state {}", code, state);
        try {
            NaverLoginResponse loginResponse = naverAuthenticationService.handleLogin(code, state);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(loginResponse.getHtmlResponse());
        } catch (Exception e){
            log.error("Naver 로그인 에러", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "네이버 로그인 실패: "+e.getMessage());
        }
    }
}
