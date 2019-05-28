package com.rguptaquantum.fabwallet.dto;

import com.rguptaquantum.fabwallet.model.Wallet;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class WalletDTO {
    private BigDecimal balance;
    private Date lastUpdated;

    public WalletDTO() {};
    public WalletDTO(Wallet wallet) {
        this.setBalance(wallet.getBalance());
        this.setLastUpdated(wallet.getLastUpdated());
    }
}
