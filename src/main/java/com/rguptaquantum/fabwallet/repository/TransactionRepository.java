package com.rguptaquantum.fabwallet.repository;

import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.Transaction;
import com.rguptaquantum.fabwallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional(rollbackOn = WalletException.class)
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    List<Transaction> getAllByWallet(Wallet wallet);
}
