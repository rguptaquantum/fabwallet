package com.rguptaquantum.fabwallet.service;

import com.rguptaquantum.fabwallet.dto.TransactionDTO;
import com.rguptaquantum.fabwallet.exception.WalletException;

import java.util.List;

public interface TransactionService {

    String addMoney(String userName, TransactionDTO transactionDTO) throws WalletException;
    String payMoney(String userName, TransactionDTO transactionDTO) throws WalletException;
    List<TransactionDTO> getTransactions(String userName) throws WalletException;
}
