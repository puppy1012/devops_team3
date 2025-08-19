package com.example.devops_team3.account.controller;

import com.example.devops_team3.account.controller.request.DeleteAccountRequest;
import com.example.devops_team3.account.controller.request.LoginAccountRequest;
import com.example.devops_team3.account.controller.request.RegisterAccountRequest;
import com.example.devops_team3.account.controller.request.UpdateAccountRequest;
import com.example.devops_team3.account.controller.response.*;
import com.example.devops_team3.account.entity.Account;
import com.example.devops_team3.account.entity.AccountLoginType;
import com.example.devops_team3.account.entity.LoginType;
import com.example.devops_team3.account.repository.AccountLoginTypeRepository;
import com.example.devops_team3.account.repository.AccountRepository;
import com.example.devops_team3.account.service.AccountIdentityService; // ★ 추가
import com.example.devops_team3.account.utility.EncryptionUtility;
import com.example.devops_team3.redis_cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountRepository accountRepository;
    private final RedisCacheService redisCacheService;
    private final AccountIdentityService accountIdentityService; // ★ 주입
    private final AccountLoginTypeRepository accountLoginTypeRepository;

    @PostMapping("/register")
    public RegisterAccountResponse register(@RequestBody RegisterAccountRequest request) {
        log.info("register -> RegisterAccountRequest: {}", request);

        AccountLoginType local = accountLoginTypeRepository.findByLoginType(LoginType.LOCAL)
                .orElseGet(() -> accountLoginTypeRepository.save(new AccountLoginType(LoginType.LOCAL)));

        Account created = accountRepository.save(request.toAccount(local));
        return RegisterAccountResponse.from(created);
    }

    @PostMapping("/login")
    public LoginAccountResponse login(@RequestBody LoginAccountRequest request) {
        log.info("login -> LoginRequest : {}", request);

        AccountLoginType type = accountLoginTypeRepository.findByLoginType(LoginType.LOCAL)
                .orElseThrow(() -> new IllegalStateException("login type not found"));

        Optional<Account> maybeAccount = accountRepository.findByEmailAndLoginType(request.getEmail(), type);
        if (maybeAccount.isEmpty()) {
            return new LoginAccountResponse("아이디와 비밀번호가 틀렸습니다.");
        }

        Account account = maybeAccount.get();
        if (!EncryptionUtility.matches(request.getPassword(), account.getPassword())) {
            return new LoginAccountResponse("아이디와 비밀번호가 틀렸습니다.");
        }

        String token = UUID.randomUUID().toString();
        // 저장 타입은 Long
        redisCacheService.setKeyAndValue(token, account.getId(), Duration.ofDays(1));

        String message = account.getNickName() + "님 으로 로그인 성공하셨습니다!";
        return LoginAccountResponse.from(token, message);
    }

    @GetMapping("/find-id")
    public IdAccountResponse getAccountId(@RequestHeader("Authorization") String token) {
        Long accountId = accountIdentityService.accountId(token);
        return new IdAccountResponse(accountId);
    }

    @PostMapping("/delete")
    public ResponseEntity<DeleteAccountResponse> delete(
            @RequestHeader("Authorization") String token,
            @RequestBody DeleteAccountRequest request) {

        Long accountId = accountIdentityService.accountId(token);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않음"));

        if (!EncryptionUtility.matches(request.getPassword(), account.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new DeleteAccountResponse("비밀번호가 틀렸습니다."));
        }

        accountRepository.delete(account);

        // 토큰 무효화
        String pure = token.substring(7).trim();
        redisCacheService.deleteByKey(pure);

        return ResponseEntity.ok(new DeleteAccountResponse("회원 탈퇴가 완료되었습니다."));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile(@RequestHeader("Authorization") String token) {
        Long accountId = accountIdentityService.accountId(token);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않음"));
        return ResponseEntity.ok(MyProfileAccountResponse.from(account));
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateAccountResponse> update(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateAccountRequest request) {

        Long accountId = accountIdentityService.accountId(token);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않음"));

        if (!EncryptionUtility.matches(request.getCurrentPassword(), account.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UpdateAccountResponse("현재 비밀번호가 일치하지 않습니다."));
        }
        if (request.getNewPassword().equals(request.getCurrentPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UpdateAccountResponse("이전 비밀번호와 동일한 비밀번호로 변경할 수 없습니다."));
        }

        account.setPassword(EncryptionUtility.encode(request.getNewPassword()));
        account.setNickName(request.getNewNickName());
        accountRepository.save(account);

        return ResponseEntity.ok(new UpdateAccountResponse("회원 정보가 성공적으로 수정되었습니다."));
    }
}
