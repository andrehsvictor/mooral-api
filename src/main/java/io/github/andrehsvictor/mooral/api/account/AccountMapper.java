package io.github.andrehsvictor.mooral.api.account;

import org.mapstruct.Mapper;

import io.github.andrehsvictor.mooral.api.shared.dto.account.AccountDto;
import io.github.andrehsvictor.mooral.api.user.User;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto userToAccountDto(User user);

}
