package com.rguptaquantum.fabwallet.repository;

import com.rguptaquantum.fabwallet.model.Transaction;
import com.rguptaquantum.fabwallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    List<Transaction> getAllByWallet(Wallet wallet);
}
