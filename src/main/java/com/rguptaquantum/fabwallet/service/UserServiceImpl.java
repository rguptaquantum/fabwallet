package com.rguptaquantum.fabwallet.service;

import com.rguptaquantum.fabwallet.dto.UserDTO;
import com.rguptaquantum.fabwallet.exception.ErrorCode;
import com.rguptaquantum.fabwallet.exception.WalletException;
import com.rguptaquantum.fabwallet.model.*;
import com.rguptaquantum.fabwallet.repository.UserRepository;
import com.rguptaquantum.fabwallet.security.JwtTokenProvider;
import com.rguptaquantum.fabwallet.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.rguptaquantum.fabwallet.exception.ErrorMessage.USER_ALREADY_EXIST;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Autowired
    private Validator validator;

    @Override
    public AuthenticationToken signin(UserDTO userDTO) throws WalletException {

        String error;
        User user = userRepository.findByUsernameAndPassword(userDTO.getUsername(),userDTO.getPassword());
        error = String.format(USER_ALREADY_EXIST,userDTO.getUsername());
        validator.isTrue(user!=null,USER_ALREADY_EXIST, ErrorCode.BadRequest.getCode());
        return jwtTokenProvider.createToken(userDTO.getUsername());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = WalletException.class)
    public void signup(UserDTO userDTO) throws WalletException {
        User existingUser = userRepository.findByUsername(userDTO.getUsername());
        validator.isTrue(existingUser==null,USER_ALREADY_EXIST, ErrorCode.BadRequest.getCode());
        User user = new User(userDTO);
        Wallet wallet = walletService.createWalletForUser(user);
        userRepository.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }
}
