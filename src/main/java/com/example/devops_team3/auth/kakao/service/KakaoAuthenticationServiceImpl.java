package com.example.devops_team3.auth.kakao.service;

import com.example.devops_team3.auth.kakao.repository.KakaoAuthenticationRepository;
import com.example.devops_team3.auth.kakao.service.response.ExistingUserKakaoLoginResponse;
import com.example.devops_team3.auth.kakao.service.response.KakaoLoginResponse;
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
public class KakaoAuthenticationServiceImpl implements KakaoAuthenticationService {

    final private AccountLoginTypeRepository accountLoginTypeRepository;
    final private KakaoAuthenticationRepository kakaoAuthRepository;
    final private RedisCacheService redisCacheService;
    final private AccountRepository accountRepository;
    final private FrontendConfig frontendConfig;


    @Override
    public KakaoLoginResponse handleLogin(String code) {

        // 실제 카카오 서버가 제공하는 access token 구하는 과정
        Map<String,Object> tokenResponse = kakaoAuthRepository.getAccessToken(code);
        String accessToken = (String) tokenResponse.get("access_token");

        // 인증된 사용자의 개인 정보들을 얻는 파트
        Map<String,Object> userInfo = kakaoAuthRepository.getUserInfo(accessToken);
        String nickname = extractNickname(userInfo);
        String email = extractEmail(userInfo);

        // 카카오 미동의 하면 실패처리
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("카카오 이메일 제공에 동의해야 합니다.");
        }

        AccountLoginType kakaoType = accountLoginTypeRepository.findByLoginType(LoginType.KAKAO)
                .orElseGet(()-> accountLoginTypeRepository.save(new AccountLoginType(LoginType.KAKAO)));

        String origin = frontendConfig.getOrigins().get(0);

        Optional<Account> optionalAccount =
                accountRepository.findByEmailAndLoginType(email, kakaoType);

        if(optionalAccount.isEmpty()){
            accountRepository.save(new Account(email, null, nickname, kakaoType));
            optionalAccount = accountRepository.findByEmailAndLoginType(email, kakaoType);
        }

        Account account = optionalAccount.get();
        String userToken = createUserTokenWithAccessToken(account, accessToken);
        return new ExistingUserKakaoLoginResponse(userToken, nickname, email, origin);
    }

    private String extractNickname(Map<String,Object> userInfo) {
        return (String) ((Map<?,?>) userInfo.get("properties")).get("nickname");
    }
    private String extractEmail(Map<String,Object> userInfo) {
        return (String) ((Map<?,?>) userInfo.get("kakao_account")).get("email");
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
