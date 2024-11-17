package com.dws.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class Account {

  @NotNull
  @NotEmpty
  private final String accountId;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal balance;

  public Account(String accountId) {
    this.accountId = accountId;
    this.balance = BigDecimal.ZERO;
  }

  @JsonCreator
  public Account(@JsonProperty("accountId") String accountId,
    @JsonProperty("balance") BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }

  public void withdraw(BigDecimal amount) {
    if(this.balance.compareTo(amount) < 0){
      throw new IllegalArgumentException("Insufficient funds for transfer.");
    }
    this.balance = this.balance.subtract(amount);
  }

  public void deposit(BigDecimal amount) {
    this.balance = this.balance.add(amount);
  }
}
