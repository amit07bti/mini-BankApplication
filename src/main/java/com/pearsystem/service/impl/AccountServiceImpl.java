package com.pearsystem.service.impl;

import com.pearsystem.dto.AccountDto;
import com.pearsystem.entity.Account;
import com.pearsystem.exception.AccountException;
import com.pearsystem.mapper.AccountMapper;
import com.pearsystem.repository.AccountRepository;
import com.pearsystem.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account= AccountMapper.mapToAccount(accountDto);
       Account savedAccount= accountRepository.save(account);
       return AccountMapper.mapToAccountDto(savedAccount);

    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account=accountRepository
                .findById(id)
                .orElseThrow(() ->new AccountException("Account does not exists"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account=accountRepository
                .findById(id)
                .orElseThrow(() ->new AccountException("Account does not exists"));
      double total = account.getBalance() +amount;
      account.setBalance(total);
      Account savedAccount=accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {

        Account account=accountRepository
                .findById(id)
                .orElseThrow(() ->new AccountException("Account does not exists"));

        if(account.getBalance() <amount){
            throw  new AccountException("Insufficient  amount");
        }
        double total=account.getBalance() -amount;
        account.setBalance(total);
        Account savedAccount=accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
       List<Account> accounts= accountRepository.findAll();
     return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
               .collect(Collectors.toList());

    }

    @Override
    public void deleteAccount(Long id) {

        Account account=accountRepository
                .findById(id)
                .orElseThrow(() ->new AccountException("Account does not exists"));

        accountRepository.deleteById(id);

    }
}
