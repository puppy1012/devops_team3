package com.example.devops_team3.auth.naver.service;

import com.example.devops_team3.auth.naver.repository.NaverAuthenticationRepository;
import com.example.devops_team3.auth.naver.service.response.ExistingUserNaverLoginResponse;
import com.example.devops_team3.auth.naver.service.response.NaverLoginResponse;
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
public class NaverAuthenticationServiceImpl implements NaverAuthenticationService {

    final private AccountLoginTypeRepository accountLoginTypeRepository;
    final private NaverAuthenticationRepository naverAuthRepository;
    final private RedisCacheService redisCacheService;
    final private AccountRepository accountRepository;
    final private FrontendConfig frontendConfig;


    @Override
    public NaverLoginResponse handleLogin(String code, String state) {

        // 실제 네이버 서버가 제공하는 access token 구하는 과정
        Map<String,Object> tokenResponse = naverAuthRepository.getAccessToken(code, state);
        String accessToken = (String) tokenResponse.get("access_token");

        // 인증된 사용자의 개인 정보들을 얻는 파트
        Map<String,Object> userInfo = naverAuthRepository.getUserInfo(accessToken);
        String nickname = extractNickname(userInfo);
        String email = extractEmail(userInfo);

        // 네이버 미동의 하면 실패처리
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("네이버 이메일 제공에 동의해야 합니다.");
        }

        AccountLoginType naverType = accountLoginTypeRepository.findByLoginType(LoginType.NAVER)
                .orElseGet(()-> accountLoginTypeRepository.save(new AccountLoginType(LoginType.NAVER)));

        String origin = frontendConfig.getOrigins().get(0);

        Optional<Account> optionalAccount =
                accountRepository.findByEmailAndLoginType(email, naverType);

        if(optionalAccount.isEmpty()){
            accountRepository.save(new Account(email, null, nickname, naverType));
            optionalAccount = accountRepository.findByEmailAndLoginType(email, naverType);
        }

        Account account = optionalAccount.get();
        String userToken = createUserTokenWithAccessToken(account, accessToken);
        return new ExistingUserNaverLoginResponse(userToken, nickname, email, origin);
    }

    private String extractNickname(Map<String,Object> userInfo) {
        Map<String,Object> response = (Map<String, Object>) userInfo.get("response");
        return (String) response.get("name");
    }
    private String extractEmail(Map<String,Object> userInfo) {
        Map<String,Object> response = (Map<String, Object>) userInfo.get("response");
        return (String) response.get("email");
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
