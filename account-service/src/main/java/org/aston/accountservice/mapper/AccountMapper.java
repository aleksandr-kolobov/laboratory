package org.aston.accountservice.mapper;

import org.aston.accountservice.dto.response.AccountResponseDto;
import org.aston.accountservice.dto.response.GetAccountsListResponseDto;
import org.aston.accountservice.persistent.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponseDto accountToAccountDto(Account customer);

    List<GetAccountsListResponseDto> listAccountToGetAccountsListResponseDto(List<Account> customer);
}
