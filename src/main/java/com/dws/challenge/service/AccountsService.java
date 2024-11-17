package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired private NotificationService notificationService;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  public void transfer(String accountFromId, String accountToId, BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Transfer amount must be positive.");
    }

    Account accountFrom = this.accountsRepository.getAccount(accountFromId);
    if (Objects.isNull(accountFrom)) {
      throw new IllegalArgumentException(accountFromId+" account not found.");
    }

    Account accountTo = this.accountsRepository.getAccount(accountToId);
    if (Objects.isNull(accountTo)) {
      throw new IllegalArgumentException(accountToId+" account not found.");
    }

    synchronized (accountFrom) {
      synchronized (accountTo) {

        accountFrom.withdraw(amount);
        accountTo.deposit(amount);

        this.accountsRepository.updateAccount(accountFrom);
        this.accountsRepository.updateAccount(accountTo);

        notificationService.notifyAboutTransfer(accountFrom,
                "Transferred " + amount + " to account " + accountToId);
        notificationService.notifyAboutTransfer(accountTo,
                "Received " + amount + " from account " + accountFromId);
      }
    }
  }
}
