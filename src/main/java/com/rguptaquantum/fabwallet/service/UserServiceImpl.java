package com.rguptaquantum.fabwallet.service;

import com.rguptaquantum.fabwallet.dto.UserDTO;
import com.rguptaquantum.fabwallet.model.*;
import com.rguptaquantum.fabwallet.repository.UserRepository;
import com.rguptaquantum.fabwallet.repository.WalletRepository;
import com.rguptaquantum.fabwallet.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public AuthenticationToken signin(UserDTO userDTO) {

        User user = userRepository.findByUsernameAndPassword(userDTO.getUsername(),userDTO.getPassword());
        if(user!=null) {
            return jwtTokenProvider.createToken(userDTO.getUsername());
        } else {
            throw new UsernameNotFoundException("User Not Found");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public boolean signup(UserDTO userDTO) {
        User existingUser = userRepository.findByUsername(userDTO.getUsername());
        if(existingUser!=null) {
            return false;
        } else {
            User user = new User(userDTO);
            Wallet wallet = new Wallet(user);
            userRepository.save(user);
            walletRepository.save(wallet);
            return true;
        }
    }
}
