package io.github.andrehsvictor.mooral.api.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.andrehsvictor.mooral.api.shared.dto.account.AccountDto;
import io.github.andrehsvictor.mooral.api.shared.dto.account.CreateAccountDto;
import io.github.andrehsvictor.mooral.api.shared.dto.account.UpdateAccountDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/api/v1/account")
    public AccountDto get() {
        return accountService.getCurrent();
    }

    @PostMapping("/api/v1/account")
    public AccountDto create(@Valid @RequestBody CreateAccountDto createAccountDto) {
        return accountService.create(createAccountDto);
    }

    @PutMapping("/api/v1/account")
    public AccountDto update(@Valid @RequestBody UpdateAccountDto updateAccountDto) {
        return accountService.update(updateAccountDto);
    }

    @DeleteMapping("/api/v1/account")
    public ResponseEntity<Void> delete() {
        accountService.delete();
        return ResponseEntity.noContent().build();
    }

}
