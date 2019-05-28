package com.rguptaquantum.fabwallet.repository;

import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional(rollbackOn = WalletException.class)
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    User findByUsername(String username);
    User getByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
}
