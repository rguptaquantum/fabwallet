package com.rguptaquantum.fabwallet.repository;

import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.User;
import com.rguptaquantum.fabwallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional(rollbackOn = WalletException.class)
public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    Wallet getByUser(User user);
    Wallet findByUser(User user);
}
