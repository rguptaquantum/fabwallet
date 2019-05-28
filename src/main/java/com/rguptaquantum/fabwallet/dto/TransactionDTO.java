package com.rguptaquantum.fabwallet.dto;

import com.rguptaquantum.fabwallet.model.Transaction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {

    private Double amount;
    private String description;
    private String globalId;
    private String type;

    public TransactionDTO() {}
    public TransactionDTO(Transaction transaction) {
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.globalId = transaction.getGlobalId();
        this.type = transaction.getType().toString();
    }
}
