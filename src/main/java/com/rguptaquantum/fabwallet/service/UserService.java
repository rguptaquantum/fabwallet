package com.rguptaquantum.fabwallet.service;

import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.AuthenticationToken;
import com.rguptaquantum.fabwallet.dto.UserDTO;
import com.rguptaquantum.fabwallet.model.User;

public interface UserService {
    AuthenticationToken signin(UserDTO userDTO) throws WalletException;
    void signup(UserDTO userDTO) throws WalletException;
    User findUser(String username);
}
