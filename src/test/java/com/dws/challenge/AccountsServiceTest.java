package com.dws.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  void addAccount() {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  void addAccount_failsOnDuplicateId() {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }
  }

  @Test
  void transferAmount() {
    Account accountFrom = new Account("Id-123",new BigDecimal(1000));
    this.accountsService.createAccount(accountFrom);

    Account accountTo = new Account("Id-456",new BigDecimal(1000));
    this.accountsService.createAccount(accountTo);

    this.accountsService.transfer(
            "Id-123","Id-456", new BigDecimal(100));

    Account debitedAccount = this.accountsService.getAccount("Id-123");
    assertThat(debitedAccount.getBalance()).isEqualByComparingTo("900");

    Account creditedAccount = this.accountsService.getAccount("Id-456");
    assertThat(creditedAccount.getBalance()).isEqualByComparingTo("1100");
  }

  @Test
  void transferAmount_WhenAmountIsNotPositive() {
    Account accountFrom = new Account("Id-123",new BigDecimal(1000));
    this.accountsService.createAccount(accountFrom);

    Account accountTo = new Account("Id-456",new BigDecimal(1000));
    this.accountsService.createAccount(accountTo);

    try {
      this.accountsService.transfer(
              "Id-123","Id-456", new BigDecimal(0));
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage()).isEqualTo("Transfer amount must be positive.");
    }
  }

  @Test
  void transferAmount_WhenAccountFromIdNotFound() {
    Account accountFrom = new Account("Id-123");
    this.accountsService.createAccount(accountFrom);

    Account accountTo = new Account("Id-456",new BigDecimal(1000));
    this.accountsService.createAccount(accountTo);

    try {
      this.accountsService.transfer(
              "123","Id-456", new BigDecimal(100));
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage()).isEqualTo(123+" account not found.");
    }
  }

  @Test
  void transferAmount_WhenAccountToIdNotFound() {
    Account accountFrom = new Account("Id-123",new BigDecimal(1000));
    this.accountsService.createAccount(accountFrom);

    Account accountTo = new Account("Id-456",new BigDecimal(1000));
    this.accountsService.createAccount(accountTo);

    try {
      this.accountsService.transfer(
              "Id-123","456", new BigDecimal(100));
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage()).isEqualTo(456+" account not found.");
    }
  }

  @Test
  void transferAmount_WhenDebitedAccountHasInsufficientFund() {
    Account accountFrom = new Account("Id-123",new BigDecimal(1000));
    this.accountsService.createAccount(accountFrom);

    Account accountTo = new Account("Id-456",new BigDecimal(1000));
    this.accountsService.createAccount(accountTo);

    try {
      this.accountsService.transfer(
              "Id-123","Id-456", new BigDecimal(2000));
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage()).isEqualTo("Insufficient funds for transfer.");
    }
  }
}
