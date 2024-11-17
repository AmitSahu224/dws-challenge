package com.dws.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferRequest {
    @NotNull
    @NotEmpty
    private String accountFrom;
    @NotNull
    @NotEmpty
    private String accountTo;
    @NotNull
    @Min(value = 0, message = "Amount must be positive.")
    private BigDecimal amount;

    @JsonCreator
    public TransferRequest(@JsonProperty("accountFrom") String accountFrom,
                           @JsonProperty("accountTo") String accountTo,
                   @JsonProperty("amount") BigDecimal amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }
}
