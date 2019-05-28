package com.rguptaquantum.fabwallet.service;

import com.rguptaquantum.fabwallet.model.AuthenticationToken;
import com.rguptaquantum.fabwallet.dto.UserDTO;

public interface UserService {
    AuthenticationToken signin(UserDTO userDTO);
    boolean signup(UserDTO userDTO);
}
