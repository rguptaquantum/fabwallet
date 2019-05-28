package com.rguptaquantum.fabwallet.repository;

import com.rguptaquantum.fabwallet.model.User;
import com.rguptaquantum.fabwallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    Wallet getByUser(User user);
}
