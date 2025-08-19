// com.example.devops_team3.account.service.AccountIdentityService
package com.example.devops_team3.account.service;

import com.example.devops_team3.account.repository.AccountRepository;
import com.example.devops_team3.redis_cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountIdentityService {
    private final RedisCacheService redisCacheService;
    private final AccountRepository accountRepository;

    public Long accountId(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 형식 오류");

        String token = authorization.substring(7).trim();
        Long accountId = redisCacheService.getValueByKey(token, Long.class); // 저장 타입(Long)과 일치
        if (accountId == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 만료/무효");

        if (!accountRepository.existsById(accountId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 없음");

        return accountId;
    }
}
