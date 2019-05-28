package com.rguptaquantum.fabwallet.dto;

import com.rguptaquantum.fabwallet.exception.ErrorCode;
import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.Transaction;
import lombok.Getter;
import lombok.Setter;

import static com.rguptaquantum.fabwallet.exception.ErrorMessage.INVALID_AMOUNT;

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

    public void validate() throws WalletException {
        if(this.amount==null&&this.amount<0) {
            String message = String.format(INVALID_AMOUNT);
            throw new WalletException(message, ErrorCode.BadRequest.getCode());
        }
    }
}
