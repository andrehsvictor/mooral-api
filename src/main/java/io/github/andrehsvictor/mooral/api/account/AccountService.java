package io.github.andrehsvictor.mooral.api.account;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.andrehsvictor.mooral.api.shared.dto.account.AccountDto;
import io.github.andrehsvictor.mooral.api.shared.dto.account.CreateAccountDto;
import io.github.andrehsvictor.mooral.api.shared.dto.account.UpdateAccountDto;
import io.github.andrehsvictor.mooral.api.shared.jwt.JwtService;
import io.github.andrehsvictor.mooral.api.user.Authority;
import io.github.andrehsvictor.mooral.api.user.AuthorityService;
import io.github.andrehsvictor.mooral.api.user.User;
import io.github.andrehsvictor.mooral.api.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;

    public AccountDto create(CreateAccountDto createAccountDto) {
        User user = accountMapper.createAccountDtoToUser(createAccountDto);
        user.setPassword(passwordEncoder.encode(createAccountDto.getPassword()));
        Authority authority = authorityService.getByName("ROLE_USER");
        user.getAuthorities().add(authority);
        user = userService.persist(user);
        return accountMapper.userToAccountDto(user);
    }

    public AccountDto update(UpdateAccountDto updateAccountDto) {
        User user = userService.getById(jwtService.getCurrentUserUuid());
        accountMapper.updateUserFromUpdateAccountDto(updateAccountDto, user);
        user = userService.persist(user);
        return accountMapper.userToAccountDto(user);
    }

    public void delete() {
        User user = userService.getById(jwtService.getCurrentUserUuid());
        userService.delete(user.getId());
    }

}
