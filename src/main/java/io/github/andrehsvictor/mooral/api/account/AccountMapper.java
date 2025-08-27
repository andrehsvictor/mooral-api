package io.github.andrehsvictor.mooral.api.account;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import io.github.andrehsvictor.mooral.api.shared.dto.account.AccountDto;
import io.github.andrehsvictor.mooral.api.shared.dto.account.CreateAccountDto;
import io.github.andrehsvictor.mooral.api.shared.dto.account.UpdateAccountDto;
import io.github.andrehsvictor.mooral.api.user.User;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto userToAccountDto(User user);

    User createAccountDtoToUser(CreateAccountDto createAccountDto);

    User updateUserFromUpdateAccountDto(UpdateAccountDto updateAccountDto, @MappingTarget User user);

    @AfterMapping
    default void afterMapping(@MappingTarget User user, UpdateAccountDto updateAccountDto) {
        if (updateAccountDto.getBio() != null && updateAccountDto.getBio().isBlank()) {
            user.setBio(null);
        }
    }

}
