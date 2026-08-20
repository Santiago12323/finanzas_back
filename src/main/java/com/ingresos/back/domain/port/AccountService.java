package com.ingresos.back.domain.port;

import com.ingresos.back.domain.model.Account;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account,String userId);
    Account findById(String id);
    List<Account> findByUserId(String userId);
    List<Account> findAllAccounts();
    Account updateAccount(String id, Account account);
    void deleteAccount(String id);
}