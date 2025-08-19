package com.example.devops_team3.auth.google.service;

import com.example.devops_team3.auth.google.repository.GoogleAuthenticationRepository;
import com.example.devops_team3.auth.google.service.response.ExistingUserGoogleLoginResponse;
import com.example.devops_team3.auth.google.service.response.GoogleLoginResponse;
import com.example.devops_team3.config.FrontendConfig;
import com.example.devops_team3.account.entity.Account;
import com.example.devops_team3.account.entity.AccountLoginType;
import com.example.devops_team3.account.entity.LoginType;
import com.example.devops_team3.redis_cache.RedisCacheService;
import com.example.devops_team3.account.repository.AccountLoginTypeRepository;
import com.example.devops_team3.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleAuthenticationServiceImpl implements GoogleAuthenticationService {

    final private AccountLoginTypeRepository accountLoginTypeRepository;
    final private GoogleAuthenticationRepository googleAuthRepository;
    final private RedisCacheService redisCacheService;
    final private AccountRepository accountRepository;
    final private FrontendConfig frontendConfig;


    @Override
    public GoogleLoginResponse handleLogin(String code) {

        // 실제 구글 서버가 제공하는 access token 구하는 과정
        Map<String,Object> tokenResponse = googleAuthRepository.getAccessToken(code);
        String accessToken = (String) tokenResponse.get("access_token");

        // 인증된 사용자의 개인 정보들을 얻는 파트
        Map<String,Object> userInfo = googleAuthRepository.getUserInfo(accessToken);
        String nickname = extractNickname(userInfo);
        String email = extractEmail(userInfo);

        // 구글 미동의 하면 실패처리
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("구글 이메일 제공에 동의해야 합니다.");
        }

        AccountLoginType googleType = accountLoginTypeRepository.findByLoginType(LoginType.GOOGLE)
                .orElseGet(()-> accountLoginTypeRepository.save(new AccountLoginType(LoginType.GOOGLE)));

        String origin = frontendConfig.getOrigins().get(0);

        Optional<Account> optionalAccount =
                accountRepository.findByEmailAndLoginType(email, googleType);

        if(optionalAccount.isEmpty()){
            accountRepository.save(new Account(email, null, nickname, googleType));
            optionalAccount = accountRepository.findByEmailAndLoginType(email, googleType);
        }

        Account account = optionalAccount.get();
        String userToken = createUserTokenWithAccessToken(account, accessToken);
        return new ExistingUserGoogleLoginResponse(userToken, nickname, email, origin);
    }

    private String extractNickname(Map<String,Object> userInfo) {
        return (String) userInfo.get("name");
    }
    private String extractEmail(Map<String,Object> userInfo) {
        return (String) userInfo.get("email");
    }

    private String createTemporaryUserToken(String accessToken) {
        String token = UUID.randomUUID().toString();
        redisCacheService.setKeyAndValue(token, accessToken, Duration.ofDays(1));
        return token;
    }

    private String createUserTokenWithAccessToken(Account account, String accessToken) {
        String userToken = UUID.randomUUID().toString();
        redisCacheService.setKeyAndValue(account.getId(), accessToken);
        redisCacheService.setKeyAndValue(userToken, account.getId());
        return userToken;
    }
}
