package com.rguptaquantum.fabwallet.service;

import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.TransactionType;
import com.rguptaquantum.fabwallet.model.User;
import com.rguptaquantum.fabwallet.model.Wallet;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public interface WalletService {

    Wallet findWallet(User user);
    Wallet updateWalletAmount(@NotNull Wallet wallet, @NotNull BigDecimal bigDecimal, TransactionType transactionType) throws WalletException;
    Wallet createWalletForUser(User user);
}
