package org.aston.accountservice.service.impl;

import org.aston.accountservice.dto.request.ChangeBalanceAccountRequestDto;
import org.aston.accountservice.dto.request.MasterAccountRequestDto;
import org.aston.accountservice.dto.response.AccountResponseDto;
import org.aston.accountservice.dto.response.GetAccountsListResponseDto;
import org.aston.accountservice.exception.AccountException;
import org.aston.accountservice.mapper.AccountMapper;
import org.aston.accountservice.persistent.entity.Account;
import org.aston.accountservice.persistent.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    private MasterAccountRequestDto masterAccountRequestDto;
    private String customerId;
    private Account accountFromDb;
    private ChangeBalanceAccountRequestDto changeBalanceDto;
    private GetAccountsListResponseDto getAccountsListResponseDto;
    private final String ACCOUNT_NUMBER = "12345678901234567890";
    private final String TYPE_DEBIT = "DEBIT";
    private final String STATUS_NAME = "Active";
    private final String CURRENCY_NAME = "RUB";
    private final String NAME_ACCOUNT = "Test Account";
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountMapper accountMapper;

    @BeforeEach
    public void setUp() {
        masterAccountRequestDto = new MasterAccountRequestDto("12345678901234567890");
        changeBalanceDto = new ChangeBalanceAccountRequestDto(ACCOUNT_NUMBER, BigDecimal.TEN, "+");
        customerId = "323e4467-e89b-12d3-a456-426655440000";
        accountFromDb = new Account();
        accountFromDb.setCustomerId(UUID.fromString(customerId));
        getAccountsListResponseDto = GetAccountsListResponseDto.builder()
                .accountNumber(ACCOUNT_NUMBER)
                .type(TYPE_DEBIT)
                .accountBalance(BigDecimal.TEN)
                .statusName(STATUS_NAME)
                .currencyName(CURRENCY_NAME)
                .masterAccount(true)
                .nameAccount(NAME_ACCOUNT)
                .build();
        accountFromDb = Account.builder()
                .customerId(UUID.fromString(customerId))
                .accountNumber(ACCOUNT_NUMBER)
                .type(TYPE_DEBIT)
                .accountBalance(BigDecimal.TEN)
                .statusName(STATUS_NAME)
                .currencyName(CURRENCY_NAME)
                .masterAccount(true)
                .nameAccount(NAME_ACCOUNT)
                .build();
    }

    @Test
    public void givenAddNewAmount_whenChangeBalance_thenReturnOk() {
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(accountFromDb));

        accountService.changeBalance(changeBalanceDto, customerId);

        verify(accountRepository, atLeast(1)).save(any(Account.class));
    }

    @Test
    public void givenNotValidCustomerId_whenChangeMasterAccountStatus_thenReturnBadRequest() {
        String customerId = "323e4467-e89b-12d3-a456-426655440009";

        assertThrows(AccountException.class, () -> accountService.changeMasterAccountStatus(masterAccountRequestDto, customerId));
        verify(accountRepository, never()).save(any(Account.class));
    }

    public void givenAmountOnBalanceLessNewAmount_whenChangeBalance_thenReturnBadRequest() {
        changeBalanceDto.setAction("-");
        accountFromDb.setAccountBalance(BigDecimal.ONE);

        assertThrows(AccountException.class, () -> accountService.changeBalance(changeBalanceDto, customerId));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void givenLessNewAmount_whenChangeBalance_thenReturnOk() {
        changeBalanceDto.setAction("-");
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(accountFromDb));

        accountService.changeBalance(changeBalanceDto, customerId);

        verify(accountRepository, atLeast(1)).save(any(Account.class));
    }

    @Test
    public void givenNotValidCustomerId_whenChangeBalance_thenReturnBadRequest() {
        String customerId = "323e4467-e89b-12d3-a456-426655440009";

        assertThrows(AccountException.class, () -> accountService.changeBalance(changeBalanceDto, customerId));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void givenMatchingCustomerId_whenFindAccountInfo_thenReturnAccountDto() {
        Account account = new Account();
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setCustomerId(UUID.fromString(customerId));
        AccountResponseDto accountResponseDto = AccountResponseDto.builder()
                .accountNumber(ACCOUNT_NUMBER)
                .build();

        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.of(account));
        when(accountMapper.accountToAccountDto(account)).thenReturn(accountResponseDto);

        AccountResponseDto result = accountService.getAccountInfo(ACCOUNT_NUMBER, customerId);

        assertEquals(accountResponseDto, result);
    }

    @Test
    public void givenNotExistentAccount_whenFindAccountInfo_thenThrowAccountNotFoundException() {
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.empty());

        assertThrows(AccountException.class, () -> accountService.getAccountInfo(ACCOUNT_NUMBER, customerId));
    }

    @Test
    public void givenNotMatchingCustomerId_whenFindAccountInfo_thenThrowAccountNotFoundException() {
        Account account = new Account();
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setCustomerId(UUID.fromString("353e4467-e89b-12d3-a456-426655440000"));

        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.of(account));

        assertThrows(AccountException.class, () -> accountService.getAccountInfo(ACCOUNT_NUMBER, customerId));
    }

    @Test
    public void givenValidCustomerId_whenFindAccountFilter_thenReturnAccountDtoFilterList() {
        List<Account> accountList = new ArrayList<>();
        accountList.add(accountFromDb);
        List<GetAccountsListResponseDto> getAccountsListResponseDtoList = new ArrayList<>();

        getAccountsListResponseDtoList.add(getAccountsListResponseDto);

        when(accountRepository.findAllByCustomerId(UUID.fromString(customerId))).thenReturn(accountList);
        when(accountMapper.listAccountToGetAccountsListResponseDto(accountList)).thenReturn(getAccountsListResponseDtoList);

        List<GetAccountsListResponseDto> result = accountService.findAccountList(customerId);

        assertEquals(getAccountsListResponseDtoList, result);
        assertEquals(1, result.size());
        assertEquals(ACCOUNT_NUMBER, result.get(0).getAccountNumber());
    }

    @Test
    public void givenInvalidCustomerId_whenFindAccountFilter_thenThrowAccountException() {
        UUID customerID = UUID.randomUUID();

        when(accountRepository.findAllByCustomerId(customerID)).thenReturn(Collections.emptyList());

        assertThrows(AccountException.class, () -> {
            accountService.findAccountList(String.valueOf(customerID));
        });
    }
}
