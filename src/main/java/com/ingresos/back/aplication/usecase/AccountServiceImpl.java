package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.exception.UserException;
import com.ingresos.back.domain.model.Account;
import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.AccountService;
import com.ingresos.back.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public Account createAccount(Account account, String userId) {
        User user = userService.findById(userId);
        account.setUser(user);
        return accountRepository.createAccount(account);
    }

    @Override
    public Account findById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new UserException("Cuenta no encontrada con id: " + id));
    }

    @Override
    public List<Account> findByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account updateAccount(String id, Account account) {
        findById(id);
        return accountRepository.updateAccount(id, account);
    }

    @Override
    public void deleteAccount(String id) {
        findById(id);
        accountRepository.deleteById(id);
    }
}