package com.ingresos.back.infrastructure.controller.account;

import com.ingresos.back.domain.model.Account;
import com.ingresos.back.domain.port.AccountService;
import com.ingresos.back.infrastructure.controller.dto.AccountDto;
import com.ingresos.back.infrastructure.mapper.AccountDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountDtoMapper accountDtoMapper;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto dto) {
        Account account = accountDtoMapper.toDomain(dto);
        return new ResponseEntity<>(
                accountService.createAccount(account, dto.getUserId()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(accountDtoMapper.toDto(accountService.findById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountDto>> findByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(accountService.
                findByUserId(userId)
                .stream()
                .map(accountDtoMapper::toDto)
                .toList());
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAllAccounts() {
        return ResponseEntity.ok(accountService.findAllAccounts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String id, @RequestBody Account account) {
        return ResponseEntity
                .ok(accountDtoMapper.toDto(accountService.updateAccount(id, account)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}