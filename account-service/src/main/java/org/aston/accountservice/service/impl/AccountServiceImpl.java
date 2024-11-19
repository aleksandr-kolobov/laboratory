package org.aston.accountservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.accountservice.dto.request.ChangeBalanceAccountRequestDto;
import org.aston.accountservice.dto.request.MasterAccountRequestDto;
import org.aston.accountservice.dto.response.AccountResponseDto;
import org.aston.accountservice.dto.response.GetAccountsListResponseDto;
import org.aston.accountservice.exception.AccountException;
import org.aston.accountservice.mapper.AccountMapper;
import org.aston.accountservice.persistent.entity.Account;
import org.aston.accountservice.persistent.repository.AccountRepository;
import org.aston.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.aston.accountservice.configuration.ApplicationConstant.MASTER_ACCOUNT_STATUS_TRUE;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    private final AccountMapper accountMapper;
    private final static Integer MIN_BALANCE_ACCOUNT = 0;
    private final static String ACTION_ADD = "+";
    private final static String ACTION_SUBTRACT = "-";

    @Override
    @Transactional
    public void changeMasterAccountStatus(MasterAccountRequestDto masterAccountRequestDto, String customerId) {
        Account accountFromDb = findAccountByNumber(masterAccountRequestDto.getAccountNumber());
        checkOwnerAccount(customerId, accountFromDb.getCustomerId());
        swapMasterAccountStatusOnFalse(customerId);
        accountFromDb.setMasterAccount(MASTER_ACCOUNT_STATUS_TRUE);
        repository.save(accountFromDb);
    }

    @Override
    public AccountResponseDto getAccountInfo(String accountNumber, String customerId) {
        log.info("Поиск информации по счету: {}", accountNumber);
        Account accountFromDb = repository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    throw new AccountException("Счет не существует");
                });

        if (String.valueOf(accountFromDb.getCustomerId()).equals(customerId)) {
            AccountResponseDto accountResponseDto = accountMapper.accountToAccountDto(accountFromDb);
            log.info("Найдена информация по счету: {}", accountNumber);
            return accountResponseDto;
        } else {
            throw new AccountException("Несоответствие идентификатора клиента");
        }
    }

    @Override
    @Transactional
    public void changeBalance(ChangeBalanceAccountRequestDto changeBalanceDto, String customerId) {
        BigDecimal amountDto = changeBalanceDto.getAmount();
        BigDecimal amountDb;

        Account accountFromDb = findAccountByNumber(changeBalanceDto.getAccountNumber());
        amountDb = accountFromDb.getAccountBalance();

        checkOwnerAccount(customerId, accountFromDb.getCustomerId());

        switch (changeBalanceDto.getAction()) {
            case ACTION_ADD -> accountFromDb.setAccountBalance(amountDb.add(amountDto));

            case ACTION_SUBTRACT -> {
                checkPositiveBalance(amountDb, amountDto);
                accountFromDb.setAccountBalance(amountDb.subtract(amountDto));
            }
        }
        repository.save(accountFromDb);
        log.info("Изменение счета = {} прошло успешно", changeBalanceDto.getAccountNumber());
    }

    private Account findAccountByNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).orElseThrow(() ->
                new AccountException("Счет = " + accountNumber + " не существует"));
    }

    private void checkOwnerAccount(String customerId, UUID customerIdAccount) {
        if (!customerIdAccount.toString().equals(customerId)) {
            throw new AccountException(HttpStatus.BAD_REQUEST, "Счет не принадлежит клиенту = " + customerId);
        }
    }

    private void swapMasterAccountStatusOnFalse(String customerId) {
        List<Account> accounts = repository.findAllByCustomerId(UUID.fromString(customerId));
        accounts.stream()
                .filter(Account::getMasterAccount)
                .forEach(account -> account.setMasterAccount(false));
        repository.saveAll(accounts);
    }

    private void checkPositiveBalance(BigDecimal amountDb, BigDecimal amountDto) {
        if (amountDb.compareTo(amountDto) < MIN_BALANCE_ACCOUNT) {
            throw new AccountException(HttpStatus.BAD_REQUEST, "Сумма списания превышает состояние счета");
        }
    }

    @Override
    public List<GetAccountsListResponseDto> findAccountList(String customerId) {
        List<Account> accountList = repository.findAllByCustomerId(UUID.fromString(customerId));
        if(accountList.isEmpty()){
            throw new AccountException("Счет не существует");
        }
        return accountMapper.listAccountToGetAccountsListResponseDto(accountList);
    }
}



