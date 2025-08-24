package io.github.andrehsvictor.mooral.api.account;

import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.shared.dto.account.AccountDto;
import io.github.andrehsvictor.mooral.api.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;

    public AccountDto toDto(User user) {
        return accountMapper.userToAccountDto(user);
    }

}
