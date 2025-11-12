package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account){
        if(account.getUsername() == null || account.getPassword() == null || account.getPassword().length() < 4){
            return null;
        }
        if(accountRepository.findByUsername(account.getUsername()).isPresent()){
            return null;
        }
        return accountRepository.save(account);
    }

    public boolean existsByUsername(String username){
        return accountRepository.findByUsername(username).isPresent();
    }

    public Optional<Account> findByUsername(String username){
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> findByUsernameAndPassword(String username, String password){
        return accountRepository.findByUsernameAndPassword(username, password);
    }

    public Optional<Account> findById(Integer id){
        return accountRepository.findById(id);
    }
}
