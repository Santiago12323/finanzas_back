package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.model.Account;
import com.ingresos.back.infrastructure.entity.account.AccountEntity;
import com.ingresos.back.domain.mapper.AccountMapper;
import com.ingresos.back.infrastructure.repository.account.SpringDataAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AccountRepository {

    @Autowired
    private SpringDataAccountRepository springDataAccountRepository;

    @Autowired
    private AccountMapper accountMapper;

    public Account createAccount(Account account) {
        AccountEntity entity = accountMapper.toEntity(account);
        AccountEntity saved = springDataAccountRepository.save(entity);
        return accountMapper.toDomain(saved);
    }

    public Optional<Account> findById(String id) {
        return springDataAccountRepository.findById(id).map(accountMapper::toDomain);
    }

    public List<Account> findByUserId(String userId) {
        return springDataAccountRepository
                .findByUserId(userId)
                .stream()
                .map(accountMapper::toDomain)
                .toList();
    }

    public List<Account> findAll() {
        return springDataAccountRepository.findAll().stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Account updateAccount(String id, Account account) {
        AccountEntity entity = accountMapper.toEntity(account);
        entity.setId(id);
        AccountEntity updated = springDataAccountRepository.save(entity);
        return accountMapper.toDomain(updated);
    }

    public void deleteById(String id) {
        springDataAccountRepository.deleteById(id);
    }
}